/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author shane
 */
public class Player {
    Node node;
    
    AssetManager assetManager;
    BulletAppState bulletAppState;
    
    RigidBodyControl player;

    Vector3f position;
    Vector3f walkDirection = new Vector3f();
    boolean charging = true;
    
    public Geometry playerObject;

    int charge;
    int damagePercent;
    // Add to 15 total
    int speed, defense, strength;

    // Default constructor
    public Player(Node _rootNode, AssetManager _assetManager, BulletAppState _appState) {
        node = _rootNode;
        assetManager = _assetManager;
        bulletAppState = _appState;
        
        damagePercent = 0;
        speed = defense = strength = 5;
        position = new Vector3f(0, 0, 0);
    }

    
    public Player(Node _rootNode, AssetManager _assetManager, BulletAppState _appState, int _speed, int _defense, int _strength) {
        node = _rootNode;
        assetManager = _assetManager;
        bulletAppState = _appState;
        
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
        playerObject = new Geometry("PlayerObject", sphere);
        SphereCollisionShape collisionShape = new SphereCollisionShape (2f);

        player = new RigidBodyControl(collisionShape, 5);
        player.setGravity(new Vector3f(0, -30f, 0));
        player.setPhysicsLocation(new Vector3f(0, 13, 0));

        bulletAppState.getPhysicsSpace().add(player);

        playerObject.setMaterial((Material) assetManager.loadMaterial("Materials/Player.j3m"));
        playerObject.setLocalTranslation(new Vector3f (0, 13, 0));
        
        // Apply rigidbody control to player
        playerObject.addControl(player);
        
        node.attachChild(playerObject);
        
        player.applyCentralForce(new Vector3f(0, 2000f, 0));
    }
    
    public boolean IsCharging() {
        return charging;
    }
}
