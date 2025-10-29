package com.app.hld.resilience.consistency;

public class ReadYourWrites {

    public static void main(String[] args) {
        WalletNode node1 = new WalletNode(1000);
        WalletNode node2 = new WalletNode(1000);

        node1.updateBalance(node1.readBalance() + 500);
        System.out.println("User sees balance (self): " + node1.readBalance());

        System.out.println("Friend sees balance (2): " + node2.readBalance());

        node2.updateBalance(node1.readBalance());
        System.out.println("After sync, Friend sees: " + node2.readBalance());
    }
}
