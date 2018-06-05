/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author shane
 */
public class CreateScene {
    private SimpleApplication app;
    Spatial scene;
    BulletAppState bulletAppState;
    RigidBodyControl landscape;
    
    Node rootNode;

    public CreateScene(SimpleApplication _app) {
        app = _app;
        rootNode = app.getRootNode();
    }

    public void Initialize() {
        // Setup physics
        bulletAppState = new BulletAppState();

        app.getStateManager().attach(bulletAppState);

        //bulletAppState.setDebugEnabled(true); // Debug purposes - draws colliders

        app.getViewPort().setBackgroundColor(new ColorRGBA(33f/255f, 32f/255f, 34f/255f, 1f));

        app.getFlyByCamera().setEnabled(false);

        app.getCamera().setLocation(new Vector3f(0, 4.5f, 26.5f));

        SetupLight();

        SetupScene();
    }

    private void SetupScene() {
        // Load our scene from the scene composer
        Spatial scene = app.getAssetManager().loadModel("Scenes/Scene.j3o");
        rootNode.attachChild(scene);

        // Finds the object called "Arena" and attaches a RigidBodyControl to it
        Spatial arena = rootNode.getChild("Arena");
        bulletAppState.getPhysicsSpace().add(arena.getControl(RigidBodyControl.class));
	
	arena.setMaterial((Material) app.getAssetManager().loadMaterial("Materials/Level.j3m"));
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
