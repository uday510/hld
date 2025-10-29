package com.app.hld.resilience.consistency;

public class EventualConsistency {

    public static void main(String[] args) throws InterruptedException {
        WalletNode node1 = new WalletNode(1000);
        WalletNode node2 = new WalletNode(1000);

        System.out.println("Initial Balances: 1=" + node1.readBalance() + ", 2=" + node2.readBalance());

        node1.updateBalance(node1.readBalance() + 500);
        System.out.println("Immediately after deposit: 1=" + node1.readBalance() + ", 2=" + node2.readBalance());

        Thread.sleep(2000);
        node2.updateBalance(node1.readBalance());
        System.out.println("After sync delay: 1=" + node1.readBalance() + ", 2=" + node2.readBalance());
    }
}
