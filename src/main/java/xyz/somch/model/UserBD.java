/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.somch.model;

import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import xyz.somch.hibernate.HibernateUtil;

/**
 *
 * @author dark_
 */
public class UserBD implements UserDAO {

    @Override
    public List<User> findByID(String id) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesion = factory.getCurrentSession();
        sesion.getTransaction().begin();
        String hql = "FROM User user WHERE user.id =:id";
        Query<User> query = sesion.createQuery(hql);
        query.setParameter("id", id);
        List<User> users = query.getResultList();
        sesion.close();
        return users;
    }

    @Override
    public List<User> findByNombre(String nombre) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesion = factory.getCurrentSession();
        sesion.getTransaction().begin();
        String hql = "FROM User user WHERE user.nombre =:nombre";
        Query<User> query = sesion.createQuery(hql);
        query.setParameter("nombre", nombre);
        List<User> users = query.getResultList();
        sesion.close();
        System.out.println("ID: "+users.get(0).getId());
        return users;
    }

    @Override
    public List<User> findAll() {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesion = factory.getCurrentSession();
        sesion.getTransaction().begin();
        String hql = "FROM User";
        Query<User> query = sesion.createQuery(hql);
        sesion.close();
        return query.getResultList();
    }

    @Override
    public boolean insertarUsuario(User user) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesion = factory.getCurrentSession();
        sesion.getTransaction().begin();
        sesion.persist(user);
        sesion.getTransaction().commit();
        return true;
    }
    
    @Override
    public boolean actualizarUsuario(User oldUser, User newUser) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesion = factory.getCurrentSession();
        sesion.getTransaction().begin();
        System.out.println("contains");
        sesion.contains(oldUser);
        System.out.println("save or update");
        sesion.update(newUser);
        System.out.println("commit");
        sesion.getTransaction().commit();
        System.out.println("salio");
        sesion.close();
        return true;
    }

    @Override
    public boolean eliminarUsuario(User user) {
        
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesion = factory.getCurrentSession();
        sesion.getTransaction().begin();
        sesion.delete(user);
        sesion.getTransaction().commit();
        return true;
    }
}
