package mygame;

// Various imports
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.KeyInput;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.ScreenController;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.math.Vector3f;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

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
    public static void main(String[] args) {
        app = new Main();
        app.start();
    }

    @Override
    /**
     * Calls niftySetUp() to set up the main menu
     */
    public void simpleInitApp() {
        // Prevents the displaying of game statistics
        //setDisplayFps(false);
        //setDisplayStatView(false);
        // Calls nifySetUp()
        niftySetUp();
        
        
        //Setup physics
        //bulletAppState = new BulletAppState();
       // stateManager.attach(bulletAppState);

        //bulletAppState.setDebugEnabled(true); // Debug purposes - draws colliders

        //Create new player, passing in this as a SimpleApplication
        //player = new Player(this);
       // player.Initialize();

       //     viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
//
       // m.setLocation(new Vector3f(0, 4.5f, 26.5f));
      //  stateManager.attach(player);
        //SetupLight();
       // SetupScene();
    }

    @Override
    /**
     * Updates the game
     */
    public void simpleUpdate(float tpf) {
        
    }
//
    //private void SetupScene() {
        // Load our scene from the scene composer
     //   Spatial scene = assetManager.loadModel("Scenes/Scene.j3o");
      //  rootNode.attachChild(scene);

        // Finds the object called "Arena" and attaches a RigidBodyControl to it
      //  Spatial arena = rootNode.getChild("Arena");
       // bulletAppState
     //           .getPhysicsSpace().add(arena.getControl(RigidBodyControl.class
      //          ));
    //}

    //private void SetupLight() {
      //  AmbientLight aL = new AmbientLight();
       // aL.setColor(ColorRGBA.White);
       // rootNode.addLight(aL);

       // DirectionalLight dL = new DirectionalLight();
        //dL.setColor(ColorRGBA.White);
        //dL.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        //rootNode.addLight(dL);
    //}

    /**
     * Creates nifty (NiftyJmeDisplay) and passes it as a parameter to MenuScreen class
     * Calls onEnable to build the layout of the mainmenu
     */
    public void niftySetUp() {
        // Creates nifty, an object of NiftyJmedisplay
        NiftyJmeDisplay nifty = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        // Sets fly camera to false
        flyCam.setEnabled(false);
        // Creates object of MenuScreen with nifty and the application as parameters
        MenuScreen screen1 = new MenuScreen(nifty, (Application) app);
        // Allows the nifty to viewed
        guiViewPort.addProcessor(nifty);
        // Calls onEnable
        screen1.onEnable();
    }
}
