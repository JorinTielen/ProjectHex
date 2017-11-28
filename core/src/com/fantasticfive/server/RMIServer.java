package com.fantasticfive.server;

import com.fantasticfive.shared.IRemoteGame;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class RMIServer {

    private static final int portNumber = 1099;

    private IRemoteGame game = null;

    public RMIServer() {
        startRMIServer();
    }

    private void startRMIServer() {
        //Print port number for registry
        InetAddress localhost = null;
        try {
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("Server: IP Address: " + localhost.getHostAddress());
        System.out.println("Server: Port number " + portNumber);

        try {
            game = new RemoteGame(portNumber);
            System.out.println("Server: Game created");
        } catch (RemoteException e) {
            System.out.println("Server: Cannot create Game");
            System.out.println("Server: RemoteException: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        RMIServer server = new RMIServer();
    }

}
