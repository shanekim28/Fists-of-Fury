/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.state.AbstractAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.network.Client;

/**
 *
 * @author shane
 */
public class LobbyState extends AbstractAppState {

    Client client;
    ClientMain app;

    public LobbyState(Client _client, ClientMain _app) {
        client = _client;
        app = _app;
    }

    public void Init() {
        app.getInputManager().addMapping("Ready", new KeyTrigger(KeyInput.KEY_SPACE));
        app.getInputManager().addListener(aL, "Ready");

    }

    private final ActionListener aL = new ActionListener() {
        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if (name.equals("Ready")) {
                app.SetReady(true);
            }
        }
    };
}
