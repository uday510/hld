package com.app.hld.papers;

import org.apache.catalina.Executor;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;

public class MiniMapReduce<K extends  Comparable<K>, V> {

    @FunctionalInterface
    public interface Mapper<K, V> {
        void map(long offset, String line, BiConsumer<K, V> emit);
    }

    @FunctionalInterface
    public interface Reducer<K, V> {
        V reduce(K key, List<V> values);
    }

    public record Split(int id, int start, int end) {}
    record Pair<A, B>(A key, B value) {}

    private final Mapper<K, V> mapper;
    private final Reducer<K, V> reducer;
    private final Reducer<K, V> combiner;
    private final int numReducers;
    private final int splitSize;

    public MiniMapReduce(
        Mapper<K, V> mapper, Reducer<K, V> reducer, Reducer<K, V> combiner,
        int numReducers, int splitSize, int numWorkers
    ) {

        this.mapper = mapper;
        this.reducer = reducer;
        this.combiner = combiner;
        this.numReducers = numReducers;
        this.splitSize = splitSize;
    }

    public List<Split> computeSplits(byte[] data) {
        List<Split> splits = new ArrayList<>();
        for (int start = 0, id = 0; start < data.length; start += splitSize, id++) {
            splits.add(new Split(id, start, Math.min(start + splitSize, data.length)));
        }
        return splits;
    }

    public void readRecords(byte[] data, Split split, BiConsumer<Long, String> onRecord) {
        int pos = split.start();
        if (pos != 0 && data[pos -1] != '\n') {
            while (pos < data.length && data[pos] != '\n') pos++;
            pos++;
        }
        while (pos < split.end()) {
            int eol = pos;
            while (eol < data.length && data[eol] != '\n') eol++;
            onRecord.accept((long) pos, new String(data, pos, eol - pos, StandardCharsets.UTF_8));
            pos = eol + 1;
        }
    }

    private int partition(K key) {
        return Math.floorMod(key.hashCode(), numReducers);
    }

    private List<List<Pair<K, V>>> runMapTask(byte[] data, Split split) {
        List<List<Pair<K, V>>> regions = new ArrayList<>();
        for (int r = 0; r < numReducers; r++) regions.add(new ArrayList<>());

        readRecords(data, split, (offset, line) ->
                mapper.map(offset, line, (k, v) -> regions.get(partition(k)).add(new Pair<>(k, v))));

        if (combiner == null) {
            return regions;
        }

        regions.replaceAll(region -> {
            List<Pair<K, V>> combined = new ArrayList<>();
            group(region).forEach((k, v) ->
                 combined.add(new Pair<>(k, combiner.reduce(k, v)))
            );
            return combined;
        });

        return regions;
    }

    private TreeMap<K, List<V>> group(Collection<Pair<K, V>> pairs) {
        TreeMap<K, List<V>> grouped = new TreeMap<>();
        for (Pair<K, V> p : pairs) grouped.computeIfAbsent(p.key(), x -> new ArrayList<>()).add(p.value());
        return grouped;
    }

    private SortedMap<K, V> runReduceTask(int r, List<List<List<Pair<K, V>>>> mapOutputs) {
        List<Pair<K, V>> fetched = new ArrayList<>();
        for (List<List<Pair<K, V>>> mapOutput : mapOutputs)
            fetched.addAll(mapOutput.get(r));
        SortedMap<K, V> output = new TreeMap<>();
        group(fetched).forEach((k, v) -> output.put(k, reducer.reduce(k, v)));
        return output;
    }

    public List<SortedMap<K, V>> run(byte[] data) throws InterruptedException, ExecutionException {
        ExecutorService workers = Executors.newCachedThreadPool();
        try {
            List<Future<List<List<Pair<K, V>>>>> mapFutures = new ArrayList<>();
            for (Split split : computeSplits(data)) {
                mapFutures.add(workers.submit(() -> runMapTask(data, split)));
            }
            List<List<List<Pair<K, V>>>> mapOutputs = new ArrayList<>();
            for (var f : mapFutures) mapOutputs.add(f.get());

            List<Future<SortedMap<K, V>>> reduceFutures = new ArrayList<>();
            for (int r = 0; r < numReducers; r++) {
                final int part = r;
                reduceFutures.add(workers.submit(() -> runReduceTask(part, mapOutputs)));
            }
            List<SortedMap<K, V>> outputs = new ArrayList<>();
            for (var f : reduceFutures) outputs.add(f.get());
            return outputs;
        } finally {
            workers.shutdown();
        }
    }
}


