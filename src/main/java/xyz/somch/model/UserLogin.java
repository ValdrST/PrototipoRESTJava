/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.somch.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author dark_
 */
public class UserLogin extends User {
    
    private boolean sesion = false;
    
    public boolean isSesion() {
        return sesion;
    }

    public void setSesion(boolean sesion) {
        this.sesion = sesion;
    }
    
    public List<String> getFields() {
        List<String> campos = new ArrayList();
        for (Field variable : this.getClass().getDeclaredFields()) {
            campos.add(variable.getName());
        }
        return campos;
    }
    
}
