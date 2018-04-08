/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.somch.model;




import java.util.UUID;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import xyz.somch.utilidades.JsonSerializable;

/**
 *
 * @author dark_
 */
public class User implements JsonSerializable{
    private String id;
    private String nombre;
    private String password;
    private String rol;
    private String token;
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
    
    public void setUIID(){
        id = UUID.randomUUID().toString().replace("-", "");
    }
    
    @Override
    public String toString(){
        return "id: " + id + " nombre: " + nombre + " password: " + password;
    }

    @Override
    public JSONObject toJson() throws JSONException{
        JSONObject json = new JSONObject();
        json.put("id", getId());
        json.put("nombre", getNombre());
        json.put("password", getPassword());
        json.put("Rol", getRol());
        return json;

    }
    
}
