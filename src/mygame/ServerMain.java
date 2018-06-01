/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.network.ConnectionListener;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.NetworkUtility.NetworkMessage;
import mygame.NetworkUtility.ReadyMessage;

/**
 *
 * @author shane
 */
public class ServerMain extends SimpleApplication implements ConnectionListener {

    static HashMap<Integer, Boolean> playersReady = new HashMap<>();

    private Server server;

    public static void main(String[] args) {
        ServerMain app = new ServerMain();
        app.start(JmeContext.Type.Headless);

        NetworkUtility.InitializeSerializables();

    }

    @Override
    public void simpleInitApp() {
        try {
            server = Network.createServer(NetworkUtility.port);
            server.start();
        } catch (IOException e) {
            Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, e);
        }

        server.addConnectionListener(this);
        server.addMessageListener(new ServerMessageListener());
    }

    // <editor-fold defaultstate="collapsed" desc=" Networking ">
    @Override
    public void destroy() {
        server.close();
        super.destroy();
    }
    
    @Override
    public void connectionAdded(Server server, HostedConnection conn) {
        System.out.println("---");
        System.out.println("Server connections: " + server.getConnections().size());
        System.out.println("Client " + conn.getId() + " has connected. Address: " + conn.getAddress());
        System.out.println("---");
        
        server.broadcast(Filters.in(conn), new NetworkMessage("Hello, client"));
        playersReady.put(conn.getId(), false);
    }
    
    @Override
    public void connectionRemoved(Server server, HostedConnection conn) {
        System.out.println("Client " + conn.getId() + " has disconnected.");
        playersReady.remove(conn.getId());
        System.out.println(playersReady.values());
        
    }
    
    private static class ServerMessageListener implements MessageListener<HostedConnection> {
        
        @Override
        public void messageReceived(HostedConnection source, Message m) {
            if (m instanceof NetworkMessage) {
                System.out.println("Received \"" + ((NetworkMessage) m).getMessage() + "\" from client " + source.getId());
            } else if (m instanceof ReadyMessage) {
                final ReadyMessage message = (ReadyMessage) m;
                playersReady.put(message.GetID(), message.GetReady());
                System.out.println(playersReady.values());
            }
            
            for (boolean b : playersReady.values()) {
                if (!b)
                    return;
            }
        }
        
    }

// </editor-fold>

}
