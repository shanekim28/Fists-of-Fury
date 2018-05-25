/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 *
 * @author shane
 */
public class PlayerControl extends AbstractControl {

    @Override
    protected void controlUpdate(float tpf) {
        spatial.rotate(tpf, tpf, tpf);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Control cloneForSpatial(Spatial spatial) {
        PlayerControl control = new PlayerControl();
        control.setSpatial(spatial);
        return control;
    }
    
}
