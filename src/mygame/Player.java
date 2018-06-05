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
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.network.Client;
import mygame.NetworkUtility.AddImpulseToPlayerMessage;
import mygame.NetworkUtility.PlayerLeftMessage;
import mygame.NetworkUtility.UpdatePlayerLocationMessage;

/**
 *
 * @author JJ and shane
 */
public class Player extends AbstractAppState implements PhysicsCollisionListener {

    Node node;

    AssetManager assetManager;
    BulletAppState bulletAppState;
    int x = 0, y = 0;

    Vector2f inputDir = new Vector2f(0, 0);

    RigidBodyControl rb;

    Vector3f position;
    Vector3f walkDirection = new Vector3f();
    boolean charging = false;
    boolean anchored = false;
    
    int boundary = 75;

    public Geometry playerObject;

    float charge;
    int damagePercent;
    int lives = 3;
    // Add to 15 total
    int speed, defense, strength;

    SimpleApplication app;
    Client client;
    int clientId;

    // Default constructor
    public Player(SimpleApplication _app, Client _client, int _id) {
	app = _app;
	client = _client;
	clientId = _id;

	node = app.getRootNode();
	assetManager = app.getAssetManager();
	bulletAppState = app.getStateManager().getState(BulletAppState.class);

	damagePercent = 0;
	speed = defense = strength = 5;
	position = new Vector3f(0, 0, 0);
    }

    public Player(SimpleApplication _app, int _speed, int _defense, int _strength) {
	app = _app;
	node = app.getRootNode();
	assetManager = app.getAssetManager();
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
	// TODO - Make this not stupid
	// Restrict motion to the x and y axis
	rb.setLinearVelocity(rb.getLinearVelocity().multLocal(1, 1, 0));

	// Kill the player if it goes out of bounds
	if ((rb.getPhysicsLocation().x > boundary || rb.getPhysicsLocation().y > boundary * 2 || rb.getPhysicsLocation().z > boundary) || (rb.getPhysicsLocation().x < -boundary || rb.getPhysicsLocation().y < -boundary || rb.getPhysicsLocation().z < -boundary)) {
	    Die();
	}

	// Check against current client's ID against constructed ID
	if (client.getId() != clientId) {
	    return;
	}
	
	client.send(new UpdatePlayerLocationMessage(rb.getPhysicsLocation(), rb.getPhysicsRotation(), clientId));

	inputDir.x = x;
	inputDir.y = y;
	//System.out.println(inputDir.toString() + " " + charge);

	// Behaviour goes here
	if (IsCharging()) {
	    // Increment the charge until it reaches a limit
	    if (charge < 20) {
		charge += ((float) speed / 100);
	    }
	}

	// If anchored, slow down the player
	if (IsAnchored()) {
	    rb.setLinearVelocity(rb.getLinearVelocity().multLocal(0.995f, 0.995f, 0.995f));
	    rb.setAngularVelocity(rb.getAngularVelocity().multLocal(0.99f, 0.99f, 0.99f));
	}
    }

    public void Die() {
	if (lives > 0) {
	    // Reduce life count
	    lives--;
	    // Reset the player
	    rb.setLinearVelocity(new Vector3f(0, -10, 0));
	    rb.setPhysicsLocation(new Vector3f(0, 20, 0));
	}
    }

    public void TakeDamage(Player enemy) {
	damagePercent += enemy.GetStrength();
	rb.setMass(rb.getMass() / (enemy.GetStrength() * (15 - GetDefense())));
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

    public RigidBodyControl GetRigidBodyControl() {
	return rb;
    }

    void InitKeys() {
	// Set up the key listeners
	app.getInputManager().addMapping("Anchor", new KeyTrigger(KeyInput.KEY_SPACE));
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
		if (!anchored) {
		    float force = strength * charge;
		    Vector3f dir = new Vector3f(inputDir.x, inputDir.y, 0).mult(strength * charge);

		    rb.applyImpulse(dir, Vector3f.ZERO);
		    client.send(new AddImpulseToPlayerMessage(dir, client.getId()));
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
	BoxCollisionShape collisionShape = new BoxCollisionShape(new Vector3f(1, 1, 1f));

	// Create new RigidBodyControl for player
	rb = new RigidBodyControl(collisionShape, strength);
	rb.setGravity(new Vector3f(0, -30f, 0));
	rb.setPhysicsLocation(new Vector3f(0, 13, 0));

	// Add the player RigidBodyControl to the application's bullet app state
	bulletAppState.getPhysicsSpace().add(rb);

	if (client.getId() == clientId) {
	    playerObject.setMaterial((Material) assetManager.loadMaterial("Materials/Player.j3m"));
	} else {
	    playerObject.setMaterial((Material) assetManager.loadMaterial("Materials/EnemyPlayer.j3m"));
	}

	playerObject.setLocalTranslation(new Vector3f(0, 13, 0));

	// Apply rigidbody control to player
	playerObject.addControl(rb);

	// playerControl.cloneForSpatial(playerObject);
	// Attach player to root node
	node.attachChild(playerObject);
    }

    @Override
    public void collision(PhysicsCollisionEvent event) {
	
    }
}
