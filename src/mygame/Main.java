package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.KeyInput;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
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
 */
public class Main extends SimpleApplication {
    Spatial scene;
    BulletAppState bulletAppState;
    RigidBodyControl landscape;
    private boolean isRunning = true;
    Player player;
    
    public static void main(String[] args) {
	Main app = new Main();
	app.start();
    }

    @Override
    public void simpleInitApp() {
        // Setup physics
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        bulletAppState.setDebugEnabled(true);
        
        // Create new player, pass in this root node, asset manager, and physics app state
        player = new Player(this);
	player.Initialize();
	
	viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
	
	flyCam.setEnabled(false);
	
	cam.setLocation(new Vector3f(0, 4.5f, 26.5f));
	
	stateManager.attach(player);
	SetupLight();
	SetupScene();
    }
     
    @Override
    public void simpleUpdate(float tpf) {
	//TODO: add update code
        
    }
    
    private final ActionListener actionListener = new ActionListener() {
	@Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Pause") && !keyPressed) {
                isRunning = !isRunning;
            }
        }
    };
    
    private void SetupScene() {
        // Load our scene from the scene composer
        Spatial scene = assetManager.loadModel("Scenes/Scene.j3o");
        rootNode.attachChild(scene);
        
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

    
}
