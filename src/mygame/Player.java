/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author shane
 */
public class Player extends AbstractAppState {
    Node node;
    
    AssetManager assetManager;
    BulletAppState bulletAppState;
    int x = 0, y = 0;
    
    Vector2f inputDir = new Vector2f(0, 0);
    
    RigidBodyControl player;

    Vector3f position;
    Vector3f walkDirection = new Vector3f();
    boolean charging = false;
    
    public Geometry playerObject;

    int charge;
    int damagePercent;
    // Add to 15 total
    int speed, defense, strength;
    
    PlayerControl playerControl = new PlayerControl();
    
    SimpleApplication app;


    // Default constructor
    public Player(SimpleApplication _app) {
	app = _app;
        node = 	app.getRootNode();
        assetManager = 	app.getAssetManager();
        bulletAppState = app.getStateManager().getState(BulletAppState.class);
        
        damagePercent = 0;
        speed = defense = strength = 5;
        position = new Vector3f(0, 0, 0);
    }

    public Player(SimpleApplication _app, int _speed, int _defense, int _strength) {
        app = _app;
        node = 	app.getRootNode();
        assetManager = 	app.getAssetManager();
        bulletAppState = app.getStateManager().getState(BulletAppState.class);
        
        damagePercent = 0;
        speed = _speed;
        defense = _defense;
        strength = _strength;
    }
    
    @Override
    public void update(float tpf) {
	inputDir = new Vector2f(x, y);
	System.out.println(inputDir.toString());
	
	// Behaviour goes here
	if (IsCharging()) {
	    player.setGravity(Vector3f.ZERO);
	    player.setPhysicsLocation(playerObject.getLocalTranslation());
	} else {
	    player.setGravity(new Vector3f(0, -30f, 0)); 
	}
    }

    public int GetStrength() {
        return strength;
    }

    public int GetDefense() {
        return defense;
    }
    
    
    public boolean IsCharging() {
        return charging;
    }
    
    void InitKeys() {
	app.getInputManager().addMapping("Charge",  new KeyTrigger(KeyInput.KEY_SPACE));
	app.getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
	app.getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
	app.getInputManager().addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
	app.getInputManager().addMapping("Down", new KeyTrigger(KeyInput.KEY_S));


	app.getInputManager().addListener(actionListener, "Charge", "Left", "Right", "Up", "Down");
    }
    
    private final ActionListener actionListener = new ActionListener() {

	@Override
	public void onAction(String name, boolean keyPressed, float tpf) {
	    if (name.equals("Left") || name.equals("Right") || name.equals("Up") || name.equals("Down")) {
		charging = true;
		
		if (name.equals("Left")) {
		    x = -1;
		} else if (name.equals("Right")) {
		    x = 1;
		}

		if (name.equals("Left") && !keyPressed) {
		    x = 0;
		} else if (name.equals("Right") && !keyPressed) {
		    x = 0;
		}

		if (name.equals("Up")) {
		    y = 1;
		} else if (name.equals("Down")) {
		    y = -1;
		}

		if (name.equals("Up") && !keyPressed) {
		    y = 0;
		} else if (name.equals("Down") && !keyPressed) {
		    y = 0;
		}
	    }
	    
	    if ((name.equals("Left") || name.equals("Right") || name.equals("Up") || name.equals("Down")) && !keyPressed)
		charging = false;
	    
	}
	
    };

    public void Initialize() {
	// Setup actions for keystrokes
	InitKeys();
	
	// Create geometries for player
        Box box = new Box(1, 1, 1);
        playerObject = new Geometry("PlayerObject", box);
        BoxCollisionShape collisionShape = new BoxCollisionShape (new Vector3f(1, 1, 1f));

	// Create new RigidBodyControl for player
        player = new RigidBodyControl(collisionShape, 5);
        player.setGravity(new Vector3f(0, -30f, 0));
        player.setPhysicsLocation(new Vector3f(0, 13, 0));
	
	// Add the player RigidBodyControl to the application's bullet app state
        bulletAppState.getPhysicsSpace().add(player);

        playerObject.setMaterial((Material) assetManager.loadMaterial("Materials/Player.j3m"));
        playerObject.setLocalTranslation(new Vector3f (0, 13, 0));
        
        // Apply rigidbody control to player
        playerObject.addControl(player);
        
        // playerControl.cloneForSpatial(playerObject);
        
	// Attach player to root node
        node.attachChild(playerObject);
    }
    
}
