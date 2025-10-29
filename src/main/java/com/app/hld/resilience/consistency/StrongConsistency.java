package com.app.hld.resilience.consistency;

public class StrongConsistency {

    public static void main(String[] args) {
        WalletNode node1 = new WalletNode(1000);
        WalletNode node2 = new WalletNode(1000);

        System.out.println("Initial Balances: 1=" + node1.readBalance() + ", 1=" + node2.readBalance());

        int newBalance = node1.readBalance() + 500;
        node1.updateBalance(newBalance);
        node2.updateBalance(newBalance);

        System.out.println("After Deposit: 1=" + node1.readBalance() + ", 2=" + node2.readBalance());
    }
}
