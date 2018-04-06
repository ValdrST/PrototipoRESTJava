/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.somch.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import xyz.somch.db.ConexionBD;

/**
 *
 * @author dark_
 */
public class UserBD implements UserDAO {

    @Override
    public List<User> findByID(String id) {
        try {
            List<User> usuarios = new ArrayList();
            User usuario = new User();
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement stmt;
            stmt = conexion.prepareStatement("SELECT * FROM usuario WHERE id=?;");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                usuario.setId(rs.getString("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setPassword(rs.getString("password"));
                usuario.setRol(rs.getString("rol"));
                usuarios.add(usuario);
            }
            return usuarios;
            } catch (SQLException | ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        return null;
    }

    @Override
    public List<User> findByNombre(String nombre) {
        try {
            List<User> usuarios = new ArrayList();
            User usuario = new User();
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement stmt;
            stmt = conexion.prepareStatement("SELECT * FROM usuario WHERE nombre=?;");
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                usuario.setId(rs.getString("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setPassword(rs.getString("password"));
                usuario.setRol(rs.getString("rol"));
                usuarios.add(usuario);
            }
            return usuarios;
            } catch (SQLException | ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        return null;
    }

    @Override
    public List<User> findAll() {
        try {
            List<User> usuarios = new ArrayList();
            User usuario = new User();
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement stmt;
            stmt = conexion.prepareStatement("SELECT * FROM usuario;");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                usuario.setId(rs.getString("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setPassword(rs.getString("password"));
                usuario.setRol(rs.getString("rol"));
                usuarios.add(usuario);
            }
            return usuarios;
            } catch (SQLException | ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        return null;
    }

    @Override
    public List<User> findRol(String rol) {
        try {
            List<User> usuarios = new ArrayList();
            User usuario = new User();
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement stmt;
            stmt = conexion.prepareStatement("SELECT * FROM usuario WHERE rol=?;");
            stmt.setString(1, rol);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                usuario.setId(rs.getString("id"));
                usuario.setNombre(rs.getString("nombre"));
                usuario.setPassword(rs.getString("password"));
                usuario.setRol(rs.getString("rol"));
                usuarios.add(usuario);
            }
            return usuarios;
            } catch (SQLException | ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        return null;
    }

    @Override
    public boolean insertarUsuario(User user) {
        try {
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement stmt;
            stmt = conexion.prepareStatement("INSERT INTO usuario(id, nombre, password, rol) VALUES (?,?,?,?);");
            stmt.setString(1, user.getId());
            stmt.setString(2, user.getNombre());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRol());
            int resultado = stmt.executeUpdate();
            return resultado > 0;
            } catch (SQLException | ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        return false;
    }
    
    public boolean setSesion(User user, Boolean sesion){
        try {
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement stmt;
            stmt = conexion.prepareStatement("UPDATE usuario SET sesion=? WHERE id=? AND nombre=? AND password=? AND rol=?;");
            stmt.setString(2, user.getId());
            stmt.setString(3, user.getNombre());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getRol());
            stmt.setBoolean(1, sesion);
            int resultado = stmt.executeUpdate();
            return resultado > 0;
            } catch (SQLException | ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        return false;
    }
    
    public boolean getSesion(User user){
        try{
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement stmt;
            stmt = conexion.prepareStatement("SELECT sesion FROM usuario WHERE id=? AND nombre=? AND password=? AND rol=?;");
            stmt.setString(1, user.getId());
            stmt.setString(2, user.getNombre());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRol());
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return rs.getBoolean("sesion");
            }
        } catch (SQLException | ClassNotFoundException ex ) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean actualizarUsuario(User oldUser, User newUser) {
        try {
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement stmt;
            stmt = conexion.prepareStatement("UPDATE usuario SET id=?, nombre=?, password=?,rol=? WHERE id=? AND nombre=? AND password=? AND rol=?;");
            stmt.setString(1, newUser.getId());
            stmt.setString(2, newUser.getNombre());
            stmt.setString(3, newUser.getPassword());
            stmt.setString(4, newUser.getRol());
            stmt.setString(5, oldUser.getId());
            stmt.setString(6, oldUser.getNombre());
            stmt.setString(7, oldUser.getPassword());
            stmt.setString(8, oldUser.getRol());
            int resultado = stmt.executeUpdate();
            return resultado > 0;
            } catch (SQLException | ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        return false;
    }

    @Override
    public boolean eliminarUsuario(User user) {
        try {
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement stmt;
            stmt = conexion.prepareStatement("DELETE FROM usuario WHERE id=? AND nombre=? AND password=? AND rol=?;");
            stmt.setString(1, user.getId());
            stmt.setString(2, user.getNombre());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRol());
            int resultado = stmt.executeUpdate();
            return resultado > 0;
            } catch (SQLException | ClassNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        return false;
    }
    
}
