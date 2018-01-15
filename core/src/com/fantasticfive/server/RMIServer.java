package com.fantasticfive.server;

import com.fantasticfive.shared.IRemoteGame;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIServer {
    private static final Logger LOGGER = Logger.getLogger(RMIServer.class.getName());
    private static final int portNumber = 1099;
    private boolean serverStarted;

    public RMIServer() {
        serverStarted = false;
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
        LOGGER.info("Server: IP Address: " + localhost.getHostAddress()); //NOSONAR
        LOGGER.info("Server: Port number " + portNumber);

        try {
            IRemoteGame game = new RemoteGame(portNumber);
            LOGGER.info("Server: Game created");
            serverStarted = true;
        } catch (RemoteException e) {
            LOGGER.info("Server: Cannot create Game");
            LOGGER.info("Server: RemoteException: " + e.getMessage());
            LOGGER.log(Level.ALL, e.getMessage());
        }
    }

    public boolean getServerStarted() {
        return this.serverStarted;
    }
}
