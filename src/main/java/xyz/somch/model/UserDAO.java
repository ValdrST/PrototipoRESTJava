/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.somch.model;

import java.util.List;

/**
 *
 * @author dark_
 */
public interface UserDAO {
    public List<User> findByID(String id);
    public List<User> findByNombre(String nombre);
    public List<User> findAll();
    boolean insertarUsuario(User user);
    public boolean actualizarUsuario(User inUser, User outUser);
    boolean eliminarUsuario(User user);
}
