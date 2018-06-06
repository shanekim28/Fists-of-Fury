/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
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
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.NetworkUtility.AddImpulseToPlayerMessage;
import mygame.NetworkUtility.NetworkMessage;
import mygame.NetworkUtility.PlayerLeftMessage;
import mygame.NetworkUtility.ReadyMessage;
import mygame.NetworkUtility.SpawnPlayerWithIDAtLocationMessage;
import mygame.NetworkUtility.UpdatePlayerLocationMessage;

/**
 *
 * @author shane
 */
public class ServerMain extends SimpleApplication implements ConnectionListener {

    boolean started = false;
    int xOffset = -5;
    static HashMap<Integer, Boolean> playersReady = new HashMap<>();

    private static Server server;
    ServerMain app;

    public static void main(String[] args) {
        ServerMain app = new ServerMain();
        app.start(JmeContext.Type.Headless);
	
        NetworkUtility.InitializeSerializables();
        
        ClientMain.main(null);
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

    @Override
    public void simpleUpdate(float tpf) {
        if (started && server.getConnections().isEmpty()) {
            started = false;
	    app.stop();
        }
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

        for (int i = 0; i < server.getConnections().size(); i++) {
            System.out.println("	" + server.getConnection(i));
        }

        System.out.println("\nClient " + conn.getId() + " has connected. Address: " + conn.getAddress());
        System.out.println("---");

        server.broadcast(Filters.in(conn), new NetworkMessage("Hello, client"));
        playersReady.put(conn.getId(), false);

    }

    @Override
    public void connectionRemoved(Server server, HostedConnection conn) {
        server.broadcast(Filters.notIn(conn), new PlayerLeftMessage(conn.getId()));
        System.out.println("Client " + conn.getId() + " has disconnected.");
        playersReady.remove(conn.getId());
        System.out.println(playersReady.values());
    }

    private class ServerMessageListener implements MessageListener<HostedConnection> {

        @Override
        public void messageReceived(final HostedConnection source, Message m) {
            if (m instanceof NetworkMessage) {
                System.out.println("Received \"" + ((NetworkMessage) m).getMessage() + "\" from client " + source.getId());
            } else if (m instanceof ReadyMessage) {
                final ReadyMessage message = (ReadyMessage) m;
                playersReady.put(message.GetID(), message.GetReady());
                System.out.println(playersReady.values());
            }

            if (m instanceof UpdatePlayerLocationMessage) {
                final UpdatePlayerLocationMessage message = (UpdatePlayerLocationMessage) m;

                ServerMain.this.enqueue(new Callable() {
                    @Override
                    public Object call() throws Exception {
                        server.broadcast(Filters.notIn(source), new UpdatePlayerLocationMessage(message.GetLocation(), message.GetRotation(), message.GetID()));

                        return null;
                    }

                });

            }

            if (m instanceof AddImpulseToPlayerMessage) {
                final AddImpulseToPlayerMessage message = (AddImpulseToPlayerMessage) m;

                server.broadcast(Filters.notIn(source), new AddImpulseToPlayerMessage(message.GetDirection(), message.GetID()));
            }
            
            if (m instanceof PlayerLeftMessage) {
                final PlayerLeftMessage message = (PlayerLeftMessage) m;
                
                server.broadcast(Filters.notIn(source), new PlayerLeftMessage(message.GetID()));
            }

            if (!started) {
                // if message is send impulse message
                // send message to all clients except the sender to add an impulse with direction and force on specified player ID
                for (boolean b : playersReady.values()) {
                    if (!b) {
                        return;
                    }
                }

                // Tell all clients to spawn players with given locations and i as their client ID
                for (int i = 0; i < playersReady.size(); i++) {
                    server.broadcast(new SpawnPlayerWithIDAtLocationMessage(new Vector3f(xOffset, 13, 0), i));
                    xOffset += 5;
                }

                started = true;
            }

        }

    }

// </editor-fold>
}
