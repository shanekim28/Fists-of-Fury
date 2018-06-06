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
        
        ServerMain.main(null);

    }

    @Override
    public void simpleInitApp() {
        
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        /* TODO Put GUI code here
        *
        * Start server when button pressed
        * ServerMain.main(null);
        *
        * Get IP from input text
        * Start client with IP from text box when button pressed
        * ClientMain.main(null);
        *
        */

    }

}
