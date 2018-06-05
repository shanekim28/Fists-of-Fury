/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.app.LegacyApplication;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.builder.ImageBuilder;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.screen.ScreenController;


/**
 * @author ryan 
 */
public class MyStartScreen extends BaseAppState implements ScreenController {
    
    // Main application
    private SimpleApplication application;
    // Nifty gui
    private Nifty nifty;
    private boolean runner = false;
    BulletAppState bulletAppState;
    
    // Initializes variables
    public MyStartScreen(Application app, NiftyJmeDisplay nifty) {
        application = (SimpleApplication) app;
        this.nifty = nifty.getNifty();
    }
    
    public MyStartScreen() {
    }
    
    
    
    @Override
    protected void initialize(Application app) {
        //It is technically safe to do all initialization and cleanup in the
        //onEnable()/onDisable() methods. Choosing to use initialize() and
        //cleanup() for this is a matter of performance specifics for the
        //implementor.
        //TODO: initialize your AppState, e.g. attach spatials to rootNode
    }

    @Override
    protected void cleanup(Application app) {
        //TODO: clean up what you initialized in the initialize method,
        //e.g. remove all spatials from rootNode
    }

    //onEnable()/onDisable() can be used for managing things that should
    //only exist while the state is enabled. Prime examples would be scene
    //graph attachment or input listener attachment.
    @Override
    protected void onEnable() {
        //Called when the state is fully enabled, ie: is attached and
        //isEnabled() is true or when the setEnabled() status changes after the
        //state is attached.

    }

    @Override
    protected void onDisable() {
        //Called when the state was previously enabled but is now disabled
        //either because setEnabled(false) was called or the state is being
        //cleaned up.
    }

    @Override
    public void update(float tpf) {
        //TODO: implement behavior during runtime
    }

    /**
     * Bind this ScreenController to a screen. This happens right before the
     * onStartScreen STARTED and only exactly once for a screen!
     *
     * @param nifty nifty
     * @param screen screen
     */
    @Override
    public void bind(Nifty nifty, Screen screen) {

        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * called right after the onStartScreen event ENDED.
     */
    @Override
    public void onStartScreen() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * called right after the onEndScreen event ENDED.
     */
    @Override
    public void onEndScreen() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void startGame(String nextScreen) {
        runner = true;
        bulletAppState = new BulletAppState();
        application.getStateManager().attach(bulletAppState);

        //bulletAppState.setDebugEnabled(true); // Debug purposes - draws colliders

        //Create new player, passing in this as a SimpleApplication
        nifty.gotoScreen(nextScreen);
        Player player = new Player(application);
        player.Initialize();
        application.getViewPort().setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        application.getCamera().setLocation(new Vector3f(0, 4.5f, 26.5f));
        application.getStateManager().attach(player);
        SetUpLight();
        SetupScene();
    }
    
    public void quitGame() {
        ((Application) application).stop();       
    }
    
    
    private void SetUpLight() {
        AmbientLight aL = new AmbientLight();
        aL.setColor(ColorRGBA.White);
        (application.getRootNode()).addLight(aL);

        DirectionalLight dL = new DirectionalLight();
        dL.setColor(ColorRGBA.White);
        dL.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        (application.getRootNode()).addLight(dL);
    }
    
    private void SetupScene() {
        // Load our scene from the scene composer
        Spatial scene = application.getAssetManager().loadModel("Scenes/Scene.j3o");
        (application.getRootNode()).attachChild(scene);

        // Finds the object called "Arena" and attaches a RigidBodyControl to it
        Spatial arena = (application.getRootNode()).getChild("Arena");
        bulletAppState.getPhysicsSpace().add(arena.getControl(RigidBodyControl.class));
    }
   
}
