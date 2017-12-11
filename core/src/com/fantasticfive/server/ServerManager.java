package com.fantasticfive.server;

import java.util.concurrent.Callable;

public class ServerManager implements Callable<Boolean>{
    private RMIServer rmiServer;

    public ServerManager(){
    }

    @Override
    public Boolean call() throws Exception {
        if (rmiServer == null){
            rmiServer = new RMIServer();
        }
        return rmiServer.getServerStarted();
    }

}
