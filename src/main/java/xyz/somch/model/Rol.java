/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.somch.model;

import javax.persistence.*;

/**
 *
 * @author dark_
 */

@Entity
@Table(name = "ROL")
public class Rol {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "ID_ROL")
    private int id;
    @Column(name = "NOMBRE")
    private String nombre;
    
    public Rol(){}
     public Rol(String nombre){
         this.id = 1;
         this.nombre = "User"; 
     }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
