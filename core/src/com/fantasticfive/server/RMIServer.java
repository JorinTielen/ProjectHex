package com.fantasticfive.server;

import com.fantasticfive.shared.IRemoteGame;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIServer {
    private static final Logger LOGGER = Logger.getLogger( RMIServer.class.getName() );

    private String ipAddress;
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
            LOGGER.log(Level.ALL, e.getMessage());
        }
        ipAddress = localhost.getHostAddress();
        LOGGER.info("Server: IP Address: " + ipAddress);
        LOGGER.info("Server: Port number " + portNumber);

        try {
            game = new RemoteGame(portNumber);
            LOGGER.info("Server: Game created");
        } catch (RemoteException e) {
            LOGGER.info("Server: Cannot create Game");
            LOGGER.info("Server: RemoteException: " + e.getMessage());
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    public static void main(String[] args) {
        RMIServer server = new RMIServer();
    }

    public String getIpAddress(){
        return ipAddress;
    }
}
