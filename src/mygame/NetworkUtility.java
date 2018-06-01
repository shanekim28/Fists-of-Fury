/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Vector3f;
import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;

/**
 *
 * @author shane
 */
public class NetworkUtility {
    public static int port = 6000;
    
    public static void InitializeSerializables() {
        Serializer.registerClass(NetworkMessage.class);
        Serializer.registerClass(ReadyMessage.class);
	Serializer.registerClass(SpawnPlayerWithIDAtLocationMessage.class);
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
    
    @Serializable
    public static class ReadyMessage extends AbstractMessage {
        private int id;
        private boolean ready;
        
        public ReadyMessage() {
            
        }
        
        public ReadyMessage (int _id, boolean _ready) {
            id = _id;
            ready = _ready;
        }
        
        public int GetID() {
            return id;
        }
        
        public boolean GetReady() {
            return ready;
        }
    }
    
    @Serializable
    public static class SpawnPlayerWithIDAtLocationMessage extends AbstractMessage {
	Vector3f location;
	int id;
	
	public SpawnPlayerWithIDAtLocationMessage() {
	    
	}
	
	public SpawnPlayerWithIDAtLocationMessage(Vector3f _location, int _id) {
	    location = _location;
	    id = _id;
	}
	
	public Vector3f GetLocation() {
	    return location;
	}
	
	public int GetID() {
	    return id;
	}
    }
}
