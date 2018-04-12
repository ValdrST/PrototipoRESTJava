/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.somch.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import xyz.somch.utilidades.JsonSerializable;
import javax.persistence.*;

/**
 *
 * @author dark_
 */
@Entity
@Table(name = "USUARIO")
public class User implements JsonSerializable, Serializable {
    
    private String id;
    
    
    private String nombre;
    
    
    private String password;
    
    
    private List<Rol> rol = new ArrayList();
    
    
    private String token;
    
    
    private Boolean sesion;
    
    
    private String refreshToken;

    public User() {
    }
    
    @Column(name = "REFRESHTOKEN")
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    @Column(name = "TOKEN")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    @Id
    @Column(name = "ID_USUARIO", unique=true, nullable=false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Column(name = "NOMBRE")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "ROL_USUARIO", joinColumns = {
    @JoinColumn(name="ID_USUARIO", nullable = false, updatable=false)
    })
    public List<Rol> getRol() {
        return rol;
    }
    
    @Column(name = "SESION")
    public Boolean getSesion() {
        return sesion;
    }

    public void setSesion(Boolean sesion) {
        this.sesion = sesion;
    }

    public void setRol(List<Rol> rol) {
        this.rol = rol;
    }

    public void addRol(Rol rol) {
        this.rol.add(rol);
    }

    public void setUIID() {
        id = UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public String toString() {
        return "id: " + id + " nombre: " + nombre + " password: " + password;
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", getId());
        json.put("nombre", getNombre());
        json.put("password", getPassword());
        json.put("Rol", getRol());
        return json;

    }

}
