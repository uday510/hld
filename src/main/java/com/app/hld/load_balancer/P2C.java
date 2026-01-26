package com.app.hld.load_balancer;
//import java.util.List;
//import java.util.concurrent.ThreadLocalRandom;
//
//public class P2C implements LoadBalancer {
//
//    @Override
//    public Server pick(List<Server> servers, Request request) {
//        int n = servers.size();
//        if (n == 0) return null;
//        if (n == 1) return servers.get(0);
//
//        ThreadLocalRandom random = ThreadLocalRandom.current();
//
//        int i = random.nextInt(n);
//        int j;
//        do {
//            j = random.nextInt(n);
//        } while (j == i);
//
//        Server s1 = servers.get(i);
//        Server s2 = servers.get(j);
//
//        return s1.getLoad().get() <= s2.getLoad().get() ? s1 : s2;
//
//    }
//
//}
