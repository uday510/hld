package com.app.hld.networking.rpc;

import java.rmi.RemoteException;

public interface Ping {

    String testPing() throws RemoteException;
}