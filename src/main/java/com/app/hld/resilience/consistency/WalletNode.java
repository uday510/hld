package com.app.hld.resilience.consistency;

public class WalletNode {
    public volatile int balance;

    public WalletNode(int balance) {
        this.balance = balance;
    }

    public void updateBalance(int newBalance) {
        balance = newBalance;
    }

    public int readBalance() {
        return balance;
    }
}
