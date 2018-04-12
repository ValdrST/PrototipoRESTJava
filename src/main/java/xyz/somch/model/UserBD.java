/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.somch.model;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import xyz.somch.db.ConexionBD;

/**
 *
 * @author dark_
 */
public class UserBD implements UserDAO {

    @Override
    public List<User> findByID(String id) {
        Session sesion = ConexionBD.crearConexion().openSession();
        Transaction tx = sesion.beginTransaction();
        String hql = "FROM User user WHERE user.id = " + id;
        List<User> user = (List<User>) sesion.createQuery("From User user WHERE user.id = "+ id).list();
        tx.commit();
        sesion.close();
        return user;
        /*try {
            List<User> usuarios = new ArrayList();
            User usuario = new User();
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement stmt;
            stmt = conexion.prepareStatement("SELECT * FROM" + tableUserBD + "WHERE " + idUserBD + "=?;");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
            usuario.setId(rs.getString(idUserBD));
            usuario.setNombre(rs.getString(nombreBD));
            usuario.setPassword(rs.getString(passwordBD));
            usuario.setToken(rs.getString(tokenBD));
            usuario.setRefreshToken(refreshTokenBD);
            usuarios.add(usuario);
            }
            return usuarios;
            } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
            }
            return null;*/
    }

    @Override
    public List<User> findByNombre(String nombre) {
        
         // = ConexionBD.crearConexion().openSession();
        SessionFactory sessionfactory =  new Configuration().configure().buildSessionFactory();
        Session sesion = sessionfactory.openSession();
        Transaction tx = sesion.beginTransaction();
        String hql = "FROM User user WHERE user.nombre = " + nombre;
        Query query = sesion.createQuery(hql);
        tx.commit();
        sesion.close();
        return query.list();
        /*try {
            List<User> usuarios = new ArrayList();
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement stmt;
            stmt = conexion.prepareStatement("SELECT * FROM" + tableUserBD + "WHERE " + nombreBD + "=?;");
            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User usuario = new User();
                usuario.setId(rs.getString(idUserBD));
                usuario.setNombre(rs.getString(nombreBD));
                usuario.setPassword(rs.getString(passwordBD));
                usuario.setRefreshToken(rs.getString(refreshTokenBD));
                usuarios.add(usuario);
            }
            return usuarios;
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return null;*/
    }

    @Override
    public List<User> findAll() {
        Session sesion = ConexionBD.crearConexion().openSession();
        Transaction tx = sesion.beginTransaction();
        String hql = "FROM User";
        Query query = sesion.createQuery(hql);
        tx.commit();
        sesion.close();
        return query.list();
        /*try {
            List<User> usuarios = new ArrayList();
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement stmt;
            stmt = conexion.prepareStatement("SELECT " +idUserBD+", "+nombreBD+","+passwordBD+" FROM " + tableUserBD + ";");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User usuario = new User();
                usuario.setId(rs.getString(idUserBD));
                usuario.setNombre(rs.getString(nombreBD));
                usuario.setPassword(rs.getString(passwordBD));
                usuarios.add(usuario);
            }
            return usuarios;
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return null;*/
    }

    @Override
    public boolean insertarUsuario(User user) {
        Session sesion = ConexionBD.crearConexion().openSession();
        Transaction tx = sesion.beginTransaction();
        user.addRol(new Rol("User"));
        sesion.save(user);
        tx.commit();
        sesion.close();
        return true;
        /*try {
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement stmt;
            stmt = conexion.prepareStatement("INSERT INTO usuario(id,"+ nombreBD+","+passwordBD+") VALUES (?,?,?,?);");
            stmt.setString(1, user.getId());
            stmt.setString(2, user.getNombre());
            stmt.setString(3, user.getPassword());
            int resultado = stmt.executeUpdate();
            return resultado > 0;
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return false;*/
    }

    /*
    public boolean setSesion(User user, Boolean sesion) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        String hql = "UPDATE User set  sesion = :sesion WHERE id = :id_usuario";
        Query query = session.createQuery(hql);
        query.setParameter("sesion", sesion);
        query.setParameter("id_usuario", user.getId());
        int res = query.executeUpdate();
        if(res==0){
            session.close();
            return false;
        }else{
            session.close();
            return true;
        }
        try {
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement stmt;
            stmt = conexion.prepareStatement("UPDATE usuario SET sesion=? WHERE id=?;");
            stmt.setString(2, user.getId());
            stmt.setBoolean(1, sesion);
            int resultado = stmt.executeUpdate();
            return resultado > 0;
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    } 
   
    public boolean setToken(User user) {
        try {
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement stmt;
            stmt = conexion.prepareStatement("UPDATE usuario SET token=? WHERE id=?;");
            stmt.setString(1, user.getToken());
            stmt.setString(2, user.getId());
            int resultado = stmt.executeUpdate();
            return resultado > 0;
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }
    
    public String getToken(User user) {
        try {
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement stmt;
            stmt = conexion.prepareStatement("SELECT token FROM usuario WHERE id=?;");
            stmt.setString(1, user.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("sera aqui?");
                return rs.getString("token");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
    
    public boolean setRefreshToken(User user) {
        try {
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement stmt;
            stmt = conexion.prepareStatement("UPDATE usuario SET refreshToken=? WHERE id=?;");
            stmt.setString(1, user.getRefreshToken());
            stmt.setString(2, user.getId());
            int resultado = stmt.executeUpdate();
            return resultado > 0;
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }
    
    public String getRefreshToken(User user) {
        try {
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement stmt;
            stmt = conexion.prepareStatement("SELECT refreshToken FROM usuario WHERE id=?;");
            stmt.setString(1, user.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("refreshToken");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }*/

    @Override
    public boolean actualizarUsuario(User oldUser, User newUser) {
        Session sesion = ConexionBD.crearConexion().openSession();
        Transaction tx = sesion.beginTransaction();
        sesion.update(newUser);
        tx.commit();
        sesion.close();
        return true;
    }

    /*try {
            
            Connection conexion = ConexionBD.crearConexion();
            PreparedStatement stmt;
            stmt = conexion.prepareStatement("UPDATE usuario SET id=?, nombre=?, password=?,rol=?, token=?, refreshToken=? WHERE id=?;");
            stmt.setString(1, newUser.getId());
            stmt.setString(2, newUser.getNombre());
            stmt.setString(3, newUser.getPassword());
            stmt.setString(4, newUser.getRol());
            stmt.setString(5, newUser.getToken());
            stmt.setString(6, newUser.getRefreshToken());
            stmt.setString(7, oldUser.getId());
            int resultado = stmt.executeUpdate();
            return resultado > 0;
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }*/

    @Override
    public boolean eliminarUsuario(User user) {
        Session sesion = ConexionBD.crearConexion().openSession();
        User u = (User) sesion.get(User.class, user.getId());
        Transaction tx = sesion.beginTransaction();
        sesion.delete(u);
        tx.commit();
        sesion.close();
        return true;
    }
    /*try {
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
    }*/

}
