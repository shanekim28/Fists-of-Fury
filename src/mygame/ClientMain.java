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
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.system.JmeContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.NetworkUtility.SpawnLocationMessage;
import mygame.NetworkUtility.ImpulseMessage;
import mygame.NetworkUtility.NetworkMessage;

/**
 *
 * @author shane
 */
// Client creates his own Player prefab
public class ClientMain extends SimpleApplication implements ClientStateListener {    
    Spatial scene;
    BulletAppState bulletAppState;
    RigidBodyControl landscape;
    Player player;
    
    private Client client;
    int clientId;
    
    private ConcurrentLinkedQueue<String> messageQueue;
    
    
    public static void main(String[] args) {
        ClientMain app  = new ClientMain();
        app.start();
	
	
	NetworkUtility.InitializeSerializables();
    }

    @Override
    public void simpleInitApp() {
        try {
            client = Network.connectToServer("localhost", NetworkUtility.port);
            client.start();
	    
	    clientId = client.getId();
        } catch (IOException e) {
            Logger.getLogger(ClientMain.class.getName()).log(Level.SEVERE, null, e);
        }
	
	messageQueue = new ConcurrentLinkedQueue<String>();
	client.addClientStateListener(this);
	client.addMessageListener(new NetworkMessageListener());
	
	// Setup physics
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        bulletAppState.setDebugEnabled(true); // Debug purposes - draws colliders
	
	viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
	flyCam.setEnabled(false);
	cam.setLocation(new Vector3f(0, 4.5f, 26.5f));
	
	SetupLight();
	SetupScene();
    }
    
    @Override
    public void simpleUpdate(float tpf) {
	String message = messageQueue.poll();
	
	if (message != null ) {
	    fpsText.setText(message);
	} else {
	    fpsText.setText("No message");
	}
	
	
    }
    
    
    private void SetupScene() {
        // Load our scene from the scene composer
        Spatial scene = assetManager.loadModel("Scenes/Scene.j3o");
        rootNode.attachChild(scene);
        
	// Finds the object called "Arena" and attaches a RigidBodyControl to it
        Spatial arena = rootNode.getChild("Arena");
        bulletAppState.getPhysicsSpace().add(arena.getControl(RigidBodyControl.class));
    }
    
    private void SetupLight() {
	AmbientLight aL = new AmbientLight();
	aL.setColor(ColorRGBA.White);
	rootNode.addLight(aL);
	
	DirectionalLight dL = new DirectionalLight();
	dL.setColor(ColorRGBA.White);
	dL.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
	rootNode.addLight(dL);
    }
    
    //<editor-fold defaultstate="collapsed" desc="Networking">
    @Override
    public void destroy() {
	client.close();
	super.destroy();
    }
    
    @Override
    public void clientConnected(Client c) {
	System.out.println("Successfully connected to server. Client ID: " + c.getId());
	clientId = c.getId();
    }
    
    @Override
    public void clientDisconnected(Client c, DisconnectInfo info) {
	System.out.println("Disconnected from server. Reason: " + info.reason);
    }
    
    public Player GetPlayer() {
	return player;
    }
    
    public void SpawnPlayers(ArrayList<Vector3f> _locs) {
	for (int i = 0; i < _locs.size(); i++) {
	    Player p = new Player (this, _locs.get(i), clientId);
	    p.Initialize();
	    
	    if (i == clientId)
		stateManager.attach(p);
	}
    }
    
    private class NetworkMessageListener implements MessageListener<Client> {
	
	@Override
	public void messageReceived(Client source, Message m) {
	    if (m instanceof NetworkMessage) {
		NetworkMessage message = (NetworkMessage) m;
		messageQueue.add(message.getMessage());
	    } else if (m instanceof SpawnLocationMessage) {
		final SpawnLocationMessage spawnLocationMessage = (SpawnLocationMessage) m;
		
		ClientMain.this.enqueue(new Callable() {
		    @Override
		    public Object call() throws Exception {
			ArrayList<Vector3f> locs = spawnLocationMessage.GetSpawnLocations();
			SpawnPlayers(locs);
			return null;
		    }
		});
	    }
	}
	
    }
//</editor-fold>
}
