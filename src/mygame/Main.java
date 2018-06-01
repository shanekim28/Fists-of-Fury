package mygame;

import com.jme3.app.SimpleApplication;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    
    private boolean isRunning = true;
    Player player;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        // Create new player, passing in this as a SimpleApplication
        player = new Player(this);
        player.Initialize();
        stateManager.attach(player);

        
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code

    }

}
