package com.fantasticfive.server;

import com.fantasticfive.shared.IGame;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {

    private static final int portNumber = 1099;
    private static final String bindingName = "AEXBanner";

    private Registry registry = null;
    private IGame game = null;

    public RMIServer() {
        startRMIServer();
    }

    private void startRMIServer() {
        //Print port number for registry
        System.out.println("Server: Port number " + portNumber);

        try{
            game = new Game();
            System.out.println("Server: Effectenbeurs created");
        } catch (RemoteException e) {
            System.out.println("Server: Cannot create effectenbeurs");
            System.out.println("Server: RemoteException: " + e.getMessage());
            game = null;
        }

        //Create registry at port number
        try {
            registry = LocateRegistry.createRegistry(portNumber);
            System.out.println("Server: Registry created on port number " + portNumber);
        } catch (RemoteException e) {
            System.out.println("Server: Cannot create registry");
            System.out.println("Server: RemoteException: " + e.getMessage());
            registry = null;
        }

        //Bind effectenbeurs using registry
        try {
            registry.rebind(bindingName, game);
            System.out.println("Server: Effectenbeurs binded to registry");
        } catch (RemoteException e) {
            System.out.println("Server: Cannot bind effectenbeurs");
            System.out.println("Server: RemoteException: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Server: Port already in use. \nServer: Please check if the server isn't already running");
            System.out.println("Server: NullPointerException: " + e.getMessage());
        }

        if(game != null) {
            System.out.println("Server: Server started");
        } else {
            System.out.println("Server: Something went wrong with starting the server");
            System.exit(0);
        }
    }

}
