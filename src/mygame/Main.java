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
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
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
    Vector3f walkDirection = new Vector3f();
    private boolean left = false, right = false, up = false, down = false;
    
    private boolean isRunning = true;

    public static void main(String[] args) {
	Main app = new Main();
	app.start();
    }

    @Override
    public void simpleInitApp() {
	Player player = new Player();
	player.Initialize();
	
	viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
	
	inputManager.addMapping("Pause",  new KeyTrigger(KeyInput.KEY_P));
	inputManager.addListener(actionListener, "Pause");
	
	flyCam.setEnabled(false);
	
	cam.setLocation(new Vector3f(0, 4.5f, 26.5f));
	SetupLight();
	SetupScene();
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
	Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
	mat.setTexture("DiffuseMap", assetManager.loadTexture("Textures/texture.jpg"));
	mat.setColor("Diffuse", ColorRGBA.White);

	Geometry scene = (Geometry) assetManager.loadModel("Models/Scene.obj");
	scene.setMaterial(mat);
	scene.setLocalTranslation(0, 0, 0);

	rootNode.attachChild(scene);
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

     
    @Override
    public void simpleUpdate(float tpf) {
	//TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
	//TODO: add render code
    }

    public class Player {
	Vector3f position;
	Vector3f walkDirection = new Vector3f();
	
	int charge;
	int damagePercent;
	// 1 to 15
	int speed, defense, strength;
	
	// Default constructor
	public Player() {
	    damagePercent = 0;
	    speed = defense = strength = 5;
	    position = new Vector3f(0, 0, 0);
	}
	
	public Player(int _speed, int _defense, int _strength) {
	    damagePercent = 0;
	    speed = _speed;
	    defense = _defense;
	    strength = _strength;
	}
	
	public int GetStrength() {
	    return strength;
	}
	
	public int GetDefense() {
	    return defense;
	}
	
	
	public void Initialize() {
	    Sphere sphere = new Sphere(32, 32, 2f);
	    Geometry playerObject = new Geometry("PlayerObject", sphere);
	    
	    playerObject.setMaterial((Material) assetManager.loadMaterial("Materials/Player.j3m"));
	    playerObject.setLocalTranslation(new Vector3f (0, 5, 0));
	    
	    rootNode.attachChild(playerObject);
	}
	
    }
}
