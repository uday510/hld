package com.app.hld.load_balancer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class WeightedP2C implements LoadBalancer {

    @Override
    public Server pick(List<Server> servers, Request request) {
        int n = servers.size();
        if (n == 0) return null;
        if (n == 1) return servers.get(0);

        ThreadLocalRandom random = ThreadLocalRandom.current();

        int i = random.nextInt(n);
        int j;
        do {
            j = random.nextInt(n);
        } while (j == i);


        Server s1 = servers.get(i);
        Server s2 = servers.get(j);

        return s1.effectiveLoad() <= s2.effectiveLoad() ? s1 : s2;
    }

    public static void main(String[] args ) {

        LoadBalancer lb = new WeightedP2C();

        Server s1 = new Server("A", 1);
        Server s2 = new Server("B", 3);
        Server s3 = new Server("C", 7);

        List<Server> servers = List.of(s1, s2, s3);

        Map<String , Integer> picks = new ConcurrentHashMap<>();

        int iterations = (int) 1e6;

        for (int i = 0; i < iterations; i++) {
            Server chosen = lb.pick(servers, null);
            chosen.incrementLoad();
            picks.merge(chosen.getId(), 1, Integer::sum);
        }

        System.out.println("Pick distribution:");
        picks.forEach((id, count) ->
                System.out.printf("Server %s -> ~%.2f%%%n",
                        id, (count * 100.0) / iterations)
        );
    }


}
