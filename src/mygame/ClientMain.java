/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.network.Network;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.NetworkUtility.NetworkMessage;
import mygame.NetworkUtility.ReadyMessage;

/**
 *
 * @author shane
 */
public class ClientMain extends SimpleApplication implements ClientStateListener {

    LobbyState lobbyState;

    private static Client client;
    private boolean isReady = false;

    public static void main(String[] args) {
        ClientMain app = new ClientMain();
        app.start();

        NetworkUtility.InitializeSerializables();
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (isReady && stateManager.hasState(lobbyState)) {
            stateManager.detach(lobbyState);
            client.send(new NetworkMessage("Client " + client.getId() + " is ready!"));
            client.send(new ReadyMessage(client.getId(), true));
        }
    }

    @Override
    public void simpleInitApp() {
        try {
            client = Network.connectToServer("localhost", NetworkUtility.port);
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

    public void SetReady(boolean ready) {
        isReady = ready;
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

    private static class ClientMessageListener implements MessageListener<Client> {

        @Override
        public void messageReceived(Client source, Message m) {
            if (m instanceof NetworkMessage) {
                client.send(new NetworkMessage("Hello, server"));
            }
        }

    }

// </editor-fold>
}
