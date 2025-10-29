package com.app.hld.dns;

import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GeoDNSServer {

    private static final int PORT = 8053;
    private static final Map<String, Map<String, String>> geoZone = new HashMap<>();
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void main(String[] args) throws Exception {
        geoZone.put("www.udayteja.com", Map.of(
                "IN", "192.168.10.10",
                "US", "192.168.20.10",
                "EU", "192.168.30.10"
        ));

        System.out.println("GeoDNS Server running on port " + PORT);

        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            byte[] buffer = new byte[512];
            while(true) {
                DatagramPacket rqPacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(rqPacket);

                executor.submit(new GeoDNSSRequestHandler(socket, rqPacket));
            }
        } finally {
            executor.shutdownNow();
        }

    }

    static class GeoDNSSRequestHandler implements Callable<Void> {
        private final DatagramSocket socket;
        private final DatagramPacket rqPacket;

        public GeoDNSSRequestHandler(DatagramSocket socket, DatagramPacket rqPacket) {
            this.socket = socket;
            this.rqPacket = rqPacket;
        }

        @Override
        public Void call() {
            try {
                Message query = new Message(rqPacket.getData());
                Record question = query.getQuestion();
                if (question == null) return null;

                Name qname = question.getName();
                String clientIP = rqPacket.getAddress().getHostAddress();
                String region = detectRegion(clientIP);

                System.out.printf("Query: %s from %s [%s]%n", qname, clientIP, region);

                Message response = buildResponse(query, qname, question, region);
                byte[] data = response.toWire();

                DatagramPacket rsPacket = new DatagramPacket(
                        data, data.length, rqPacket.getAddress(),
                        rqPacket.getPort()
                );

                socket.send(rsPacket);

            } catch (Exception ignored) {}

            return null;
        }

        private Message buildResponse(Message query, Name qname, Record question, String region) {
            Message response = new Message();
            Header header = new Header(query.getHeader().getID());
            response.setHeader(header);
            header.setFlag(Flags.QR);
            header.setFlag(Flags.AA);
            header.unsetFlag(Flags.RD);
            header.unsetFlag(Flags.RA);

            response.addRecord(question, Section.QUESTION);

            String domain = qname.toString().toLowerCase().trim();
            if (domain.endsWith(".")) domain = domain.substring(0, domain.length() - 1);

            Map<String, String> regionMap = geoZone.get(domain);
            if (regionMap == null) {
                header.setRcode(Rcode.NXDOMAIN);
                System.out.println("No zone for: " + domain);
                return response;
            }

            String ip = regionMap.getOrDefault(region, regionMap.get("US"));
            if (ip == null) {
                header.setRcode(Rcode.NXDOMAIN);
                System.out.println("No IP found for region: " + region);
            } else {
                try {
                    InetAddress address = InetAddress.getByName(ip);
                    ARecord answer = new ARecord(qname, Type.A, 60, address);
                    response.addRecord(answer, Section.ANSWER);
                    System.out.printf("Responding to %s [%s] -> %s%n", domain, region, ip);
                } catch (Exception ignored) {}
            }

            return response;
        }

        private String detectRegion(String clientIP) {
            if (clientIP.startsWith("192.") || clientIP.startsWith("10.") || clientIP.startsWith("172.")) return "IN";
            if (clientIP.startsWith("44.") || clientIP.startsWith("35.")) return "US";
            if (clientIP.startsWith("80.") || clientIP.startsWith("90.")) return "EU";
            return "US";
        }
    }

}
