/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import com.jme3.niftygui.NiftyJmeDisplay;
import java.io.IOException;
import java.util.ArrayList;
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
public class ClientMain extends SimpleApplication implements ClientStateListener {

    ArrayList<Player> players = new ArrayList<>();

    LobbyState lobbyState;
    
    static String address = "localhost";
	MenuScreen screen1;
	static ClientMain app;

    private static Client client;
    private boolean isReady = false;

    public static void main(String[] args) {
        app = new ClientMain();
        app.start();
		
        NetworkUtility.InitializeSerializables();

        app.setPauseOnLostFocus(false);
    }
    
    public void niftySetUp() {
        // Creates nifty, an object of NiftyJmedisplay
        NiftyJmeDisplay nifty = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        // Sets fly camera to false
        flyCam.setEnabled(false);
        // Creates object of MenuScreen with nifty and the application as parameters
        screen1 = new MenuScreen(nifty, (Application) app);
        // Allows the nifty to viewed
        guiViewPort.addProcessor(nifty);
        // Calls onEnable
        screen1.onEnable();
    }

    @Override
    public void simpleInitApp() {
		niftySetUp();
    }
	
	public void JoinGame() {
		try {
            client = Network.connectToServer(address, NetworkUtility.port);
            client.start();
        } catch (IOException e) {
            Logger.getLogger(ClientMain.class.getName()).log(Level.SEVERE, null, e);
        }

        client.addClientStateListener(this);
        client.addMessageListener(new ClientMessageListener());

        new CreateScene(this).Initialize();

        lobbyState = new LobbyState(client, this);
        lobbyState.Init();
        stateManager.attach(lobbyState);
	}

    @Override
    public void simpleUpdate(float tpf) {
		if (screen1.myStartScreen.runner) {
			JoinGame();
			screen1.myStartScreen.runner = false;
		}
		
        if (isReady && stateManager.hasState(lobbyState)) {
            stateManager.detach(lobbyState);
            client.send(new NetworkMessage("Client " + client.getId() + " is ready!"));
            client.send(new ReadyMessage(client.getId(), true));
        }
    }

    public void SetReady(boolean ready) {
        isReady = ready;
    }
    
    public static void SetAddress(String _ip) {
        address = _ip;
    }

    public void SpawnPlayer(Vector3f location, int id) {
        Player p = new Player(this, client, id);
        p.Initialize();
        stateManager.attach(p);
        players.add(p);
    }

    // <editor-fold defaultstate="collapsed" desc=" Networking ">
    @Override
    public void destroy() {
        client.close();
        super.destroy();
    }

    @Override
    public void clientConnected(Client c) {

    }

    @Override
    public void clientDisconnected(Client c, DisconnectInfo info) {
	
    }

    private class ClientMessageListener implements MessageListener<Client> {

        @Override
        public void messageReceived(Client source, Message m) {
            if (m instanceof NetworkMessage) {
                client.send(new NetworkMessage("Hello, server"));
            } else if (m instanceof SpawnPlayerWithIDAtLocationMessage) {
                final SpawnPlayerWithIDAtLocationMessage message = (SpawnPlayerWithIDAtLocationMessage) m;

                ClientMain.this.enqueue(new Callable() {
                    @Override
                    public Object call() throws Exception {
                        SpawnPlayer(message.GetLocation(), message.GetID());
                        return null;
                    }

                });
            }

            if (m instanceof UpdatePlayerLocationMessage) {
                final UpdatePlayerLocationMessage message = (UpdatePlayerLocationMessage) m;

                ClientMain.this.enqueue(new Callable() {
                    @Override
                    public Object call() throws Exception {
                        players.get(message.GetID()).GetRigidBodyControl().setPhysicsLocation(message.GetLocation());
                        players.get(message.GetID()).GetRigidBodyControl().setPhysicsRotation(message.GetRotation());

                        return null;
                    }
                });

            }

            if (m instanceof AddImpulseToPlayerMessage) {
                final AddImpulseToPlayerMessage message = (AddImpulseToPlayerMessage) m;

                players.get(message.GetID()).GetRigidBodyControl().applyImpulse(message.GetDirection(), Vector3f.ZERO);
            }
            
            if (m instanceof PlayerLeftMessage) {
                final PlayerLeftMessage message = (PlayerLeftMessage) m;
                
				if (players.get(message.GetID()) != null)
					players.get(message.GetID()).node.detachChild(players.get(message.GetID()).playerObject);
            }
            
        }
    }

// </editor-fold>
}
