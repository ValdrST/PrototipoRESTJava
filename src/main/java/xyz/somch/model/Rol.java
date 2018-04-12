/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.somch.model;

import java.util.List;
import javax.persistence.*;

/**
 *
 * @author dark_
 */

@Entity
@Table(name = "ROL")
public class Rol {
    
    private int id;
    private String nombre;
    private List<User> usuarios;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "ROL_USUARIO", joinColumns = {
    @JoinColumn(name="ID_ROL", nullable = false, updatable=false)
    })
    public List<User> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<User> usuarios) {
        this.usuarios = usuarios;
    }
    
    public Rol(){}
     public Rol(String nombre){
         this.id = 1;
         this.nombre = "User"; 
     }
     
    @Id
    @Column(name = "ID_ROL")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @Column(name = "NOMBRE")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
