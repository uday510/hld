package com.app.hld.networking.rpc;

import com.app.hld.networking.rpc.Ping;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PingImpl extends UnicastRemoteObject implements Ping {
    protected PingImpl() throws RemoteException {
        super();
    }

    public String testPing() {
        return "Ping From Remote Server";
    }
    
}