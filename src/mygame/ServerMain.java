/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.ConnectionListener;
import com.jme3.network.Filters;
import com.jme3.network.HostedConnection;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.NetworkUtility.SpawnLocationMessage;
import mygame.NetworkUtility.NetworkMessage;

/**
 *
 * @author shane
 */
public class ServerMain extends SimpleApplication implements ConnectionListener {
    boolean a = true;
    
    int xOffset = 0;
    
    ArrayList<Vector3f> spawnPoints = new ArrayList<>();
    
    HashMap<Integer, Player> players = new HashMap<>();
    
    private Server server;
    
    public static void main(String[] args) {
        ServerMain app  = new ServerMain();
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
    }
    
    @Override
    public void simpleUpdate(float tpf) {
	server.broadcast(new NetworkMessage("Hello world!" + tpf));
	
	if (a && server.getConnections().size() >= 2) {
	    server.broadcast(new SpawnLocationMessage(spawnPoints));
	    
	    // TODO map every Player to each ID
	    for (HostedConnection c : server.getConnections()) {
		
	    }
	    
	    a = false;
	}
    }
    
    @Override
    public void destroy() {
	server.close();
	super.destroy();
    }

    @Override
    public void connectionAdded(Server server, HostedConnection conn) {
	System.out.println("Client " + conn.getId() + " has connected");
	System.out.println("Current connections: " + server.getConnections().toString());
	
	spawnPoints.add(new Vector3f(xOffset, 10, 0));
	xOffset += 5;
    }

    @Override
    public void connectionRemoved(Server server, HostedConnection conn) {
	System.out.println("Client " + conn.getId() + " has disconnected");
    }
}
