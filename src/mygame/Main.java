package mygame;

// Various imports
import com.jme3.app.SimpleApplication;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 * @author ryan
 */
public class Main extends SimpleApplication {

    // Scene
    //Spatial scene;
    // Used for physics
    //BulletAppState bulletAppState;
    //RigidBodyControl landscape;
    // private boolean isRunning = false;
    //Player player;
    // Application for game
    private static Main app;

    /**
     * Initializes app and starts the game
     */
    private boolean isRunning = true;
    Player player;

    public static void main(String[] args) {
        app = new Main();
        app.start();

        //ServerMain.main(null);
    }

    @Override
    /**
     * Calls niftySetUp() to set up the main menu
     */
    public void simpleInitApp() {
        // Calls niftySetUp()
        
    }

    @Override
    /**
     * Updates the game
     */
    public void simpleUpdate(float tpf) {
    }
    
    /**
     * Creates nifty (NiftyJmeDisplay) and passes it as a parameter to
     * MenuScreen class Calls onEnable to build the layout of the mainmenu
     */
    
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
