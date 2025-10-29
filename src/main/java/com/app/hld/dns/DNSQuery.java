package com.app.hld.dns;

import org.xbill.DNS.*;
import org.xbill.DNS.Record;

public class DNSQuery {

    public static void main(String[] args) throws Exception {

        String domain = "www.google.com";
        String dnsServer = "1.1.1.1";

        Lookup lookup = new Lookup(domain, Type.A);
        SimpleResolver resolver = new SimpleResolver(dnsServer);
        lookup.setResolver(resolver);

        Record[] records = lookup.run();

        System.out.println("Querying DNS server: " + dnsServer);
        for (Record record : records) {
            System.out.println(domain + " -> " + ((ARecord) record).getAddress().getHostAddress());
        }

    }
}
