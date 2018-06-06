/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.math.Quaternion;
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
	Serializer.setReadOnly(false);
        Serializer.registerClass(NetworkMessage.class);
        Serializer.registerClass(ReadyMessage.class);
        Serializer.registerClass(SpawnPlayerWithIDAtLocationMessage.class);
        Serializer.registerClass(AddImpulseToPlayerMessage.class);
        Serializer.registerClass(UpdatePlayerLocationMessage.class);
	Serializer.registerClass(UpdatePlayerMassMessage.class);
        Serializer.registerClass(PlayerLeftMessage.class);
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

        public ReadyMessage(int _id, boolean _ready) {
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

    @Serializable
    public static class AddImpulseToPlayerMessage extends AbstractMessage {

        Vector3f dir;
        int id;

        public AddImpulseToPlayerMessage() {
        }

        public AddImpulseToPlayerMessage(Vector3f _dir, int _id) {
            dir = _dir;
            id = _id;
        }

        // Although this says direction, it also means the entire force vector
        public Vector3f GetDirection() {
            return dir;
        }

        public int GetID() {
            return id;
        }
    }
    
    @Serializable
    public static class UpdatePlayerMassMessage extends AbstractMessage {

        float mass;
        int id;

        public UpdatePlayerMassMessage() {
        }

        public UpdatePlayerMassMessage(float _mass, int _id) {
            mass = _mass;
            id = _id;
        }

        // Although this says direction, it also means the entire force vector
        public float GetMass() {
            return mass;
        }

        public int GetID() {
            return id;
        }
    }

    @Serializable
    public static class UpdatePlayerLocationMessage extends AbstractMessage {
        Vector3f loc;
        Quaternion rot;
        int id;
        
        public UpdatePlayerLocationMessage() {
            
        }
        
        public UpdatePlayerLocationMessage(Vector3f _loc, Quaternion _rot, int _id) {
            loc = _loc;
            rot = _rot;
            id = _id;
        }
        
        public Vector3f GetLocation() {
            return loc;
        }
        
        public Quaternion GetRotation() {
            return rot;
        }
        
        public int GetID() {
            return id;
        }
    }
    
    @Serializable
    public static class PlayerLeftMessage extends AbstractMessage {

        int id;
        
        public PlayerLeftMessage() {

        }

        public PlayerLeftMessage(int _id) {
            id = _id;
        }
        
        public int GetID() {
            return id;
        }
    }
}
