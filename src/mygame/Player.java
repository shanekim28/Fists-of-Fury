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
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.Timer;
import com.jme3.bullet.collision.PhysicsCollisionListener;

/**
 *
 * @author shane
 */
public class Player extends AbstractAppState implements PhysicsCollisionListener {
    Node node;
    
    AssetManager assetManager;
    BulletAppState bulletAppState;
    int x = 0, y = 0;
    
    Vector2f inputDir = new Vector2f(0, 0);
    
    RigidBodyControl player;

    Vector3f position;
    Vector3f walkDirection = new Vector3f();
    boolean charging = false;
    boolean anchored = false;
    
    int boundary = 75;
    
    int jumpCharges = 2;
    
    public Geometry playerObject;

    float charge;
    int damagePercent;
    int lives = 3;
    // Add to 15 total
    int speed, defense, strength;
    
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
    
    public Vector3f getPosition() {
	return position;
    }
    
    @Override
    public void update(float tpf) {
	inputDir.x = x;
	inputDir.y = y;
	System.out.println(inputDir.toString() + " " + charge);
	
	// Behaviour goes here
	if (IsCharging()) {
	    // Increment the charge until it reaches a limit
	    if (charge < 20)
		charge += ((float) speed / 100);
	}
	
	// If anchored, slow down the player
	if (IsAnchored())
	    player.setLinearVelocity(player.getLinearVelocity().multLocal(0.995f, 0.995f, 0.995f));
	// Kill the player if it goes out of bounds
	if ((player.getPhysicsLocation().x > boundary || player.getPhysicsLocation().y > boundary * 2 || player.getPhysicsLocation().z > boundary) || (player.getPhysicsLocation().x < -boundary || player.getPhysicsLocation().y < -boundary || player.getPhysicsLocation().z < -boundary))
	    Die();
	
	// TODO - Make this not stupid
	// Restrict motion to the x and y axis
	player.setLinearVelocity(player.getLinearVelocity().multLocal(1,1,0));
    }
    
    public void Die() {
	if (lives > 0) {
	    // Reduce life count
	    lives--;
	    // Reset the player
	    player.setLinearVelocity(new Vector3f(0, -10, 0));
	    player.setPhysicsLocation(new Vector3f(0, 20, 0));
	}
    }
    
    public void TakeDamage(Player enemy) {
	damagePercent++;
	player.setMass(player.getMass() / enemy.GetStrength() * GetDefense());
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
    
    public boolean IsAnchored() {
        return anchored;
    }
    
    void InitKeys() {
	// Set up the key listeners
	
	app.getInputManager().addMapping("Anchor",  new KeyTrigger(KeyInput.KEY_SPACE));
	app.getInputManager().addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
	app.getInputManager().addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
	app.getInputManager().addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
	app.getInputManager().addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
	
	
	app.getInputManager().addListener(actionListener, "Anchor", "Left", "Right", "Up", "Down");
	
	
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

		if (name.equals("Up")) {
		    y = 1;
		} else if (name.equals("Down")) {
		    y = -1;
		}
	    }
	    
	    if ((name.equals("Left") || name.equals("Right") || name.equals("Up") || name.equals("Down")) && !keyPressed) {
		if (jumpCharges > 0) {
		    player.applyImpulse(new Vector3f(inputDir.x * strength * charge, inputDir.y * strength * charge, 0), Vector3f.ZERO);
		    //jumpCharges--;
		}
		
		charging = false;
		charge = 0;
		
		if (name.equals("Up")) {
		    y = 0;
		} else if (name.equals("Down")) {
		    y = 0;
		}
		
		if (name.equals("Left")) {
		    x = 0;
		} else if (name.equals("Right")) {
		    x = 0;
		}
	    }
	    
	    if (name.equals("Anchor")) {
		anchored = true;
	    }
	    
	    if (name.equals("Anchor") && !keyPressed) {
		anchored = false;
	    }
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
        player = new RigidBodyControl(collisionShape, strength);
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

    @Override
    public void collision(PhysicsCollisionEvent event) {
	jumpCharges = 2;
    }
}
