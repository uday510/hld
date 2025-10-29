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

public class DNSServer {

    private static final int PORT = 8053;
    private static final Map<String, String> zone = new HashMap<>();
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    public static void main(String[] args) throws Exception {
        zone.put("www.udayteja.com", "192.168.1.10");
        zone.put("api.udayteja.com", "192.168.1.10");
        zone.put("db.udayteja.com", "192.168.1.10");

        System.out.println("DNS Server running on port " + PORT);

        try(DatagramSocket socket = new DatagramSocket(PORT)) {
            byte[] buffer = new byte[512];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                executor.submit(new DNSRequestHandler(socket, packet));
            }

        } catch (Exception ignored) {}
        finally {
            shutDownExecutorNow();
        }
    }

    private static Message buildResponse(Message query, Name qname, Record question) {
        Message response = new Message();
        Header header = new Header(query.getHeader().getID());
        response.setHeader(header);
        header.setFlag(Flags.QR);
        header.setFlag(Flags.AA);
        header.setFlag(Flags.RD);
        header.unsetFlag(Flags.RA);

        response.addRecord(question, Section.QUESTION);

        String domain = qname.toString().toLowerCase().trim();
        if (domain.endsWith(".")) {
            domain = domain.substring(0, domain.length() - 1);
        }
        String ip = zone.get(domain);

        if (ip == null) {
            System.out.println("Cache has not record for: " + domain);
            header.setRcode(Rcode.NXDOMAIN);
        } else {
           try {
               InetAddress address = InetAddress.getByName(ip);
               ARecord answer = new ARecord(qname, Type.A, 60, address);
               response.addRecord(answer, Section.ANSWER);
               System.out.println("Responding: " + domain + " -> " + ip);
           } catch (Exception e) {
               header.setRcode(Rcode.SERVFAIL);
           }
        }

        return response;
    }

    private static void shutDownExecutorNow() {
        executor.shutdownNow();
    }

    static class DNSRequestHandler implements Callable<Void> {
        private final DatagramSocket socket;
        private final DatagramPacket packet;

        public DNSRequestHandler(DatagramSocket socket, DatagramPacket packet) {
            this.socket = socket;
            this.packet = packet;
        }

        @Override
        public Void call() {
            try {
                Message query = new Message(packet.getData());
                Record question = query.getQuestion();
                Name qname = question.getName();
                System.out.println("Received query for: " + qname);

                Message response = buildResponse(query, qname, question);
                byte[] data = response.toWire();

                DatagramPacket rsPacket = new DatagramPacket(
                        data,
                        data.length,
                        packet.getAddress(),
                        packet.getPort()
                );

                socket.send(rsPacket);

            } catch (Exception ignored) {}

            return null;
        }

    }
}

