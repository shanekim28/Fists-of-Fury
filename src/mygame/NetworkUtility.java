/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;
import java.util.ArrayList;

/**
 *
 * @author shane
 */

// TODO: Implement "Spawn player" message
// TODO: Implement "Add impulse" message
public class NetworkUtility {
    public static int port = 27015;
    
    public static void InitializeSerializables() {
        Serializer.registerClass(NetworkMessage.class);
	Serializer.registerClass(SpawnLocationMessage.class);
	Serializer.registerClass(AddPlayerMessage.class);
    }
    
    @Serializable
    public static class SpawnLocationMessage extends AbstractMessage {
	ArrayList<Vector3f> locs;
	
	public SpawnLocationMessage() {
	    
	}
	
	public SpawnLocationMessage(ArrayList<Vector3f> _locs) {
	    locs = _locs;
	}
	
	public ArrayList<Vector3f> GetSpawnLocations() {
	    return locs;
	}
    }
    
    @Serializable
    public static class AddPlayerMessage extends AbstractMessage {
	int clientId;
	RigidBodyControl rb;
	
	public AddPlayerMessage() {
	    
	}
	
	public AddPlayerMessage (int _clientId, RigidBodyControl _rb) {
	    rb = _rb;
	    clientId = _clientId;
	}
	
	public RigidBodyControl GetRigidBodyControl() {
	    return rb;
	}
	
	public int GetClientID() {
	    return clientId;
	}
    }
    
    @Serializable
    public static class ImpulseMessage extends AbstractMessage {
	private Vector3f dir;
	private float force;
	
	public ImpulseMessage() {
	    
	}
	
	public ImpulseMessage (Vector3f _dir, float _force) {
	    dir = _dir;
	    force = _force;
	}
	
	public Vector3f GetDir() {
	    return dir;
	}
	
	public float GetForce() {
	    return force;
	}
    }
    
    @Serializable
    public static class NetworkMessage extends AbstractMessage {
        private String message;
        
        public NetworkMessage() {
            
        }
        
        public NetworkMessage(String msg) {
            message = msg;
        }
        
        public String getMessage() {
            return message;
        }
    }
}
