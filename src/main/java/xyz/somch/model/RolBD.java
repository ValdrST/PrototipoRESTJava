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
 * @author valdr
 */
public class RolBD {

    public List<Rol> findAll() {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesion = factory.getCurrentSession();
        sesion.getTransaction().begin();
        String hql = "FROM Rol";
        Query<Rol> query = sesion.createQuery(hql);
        List<Rol> rol = query.getResultList();
        sesion.close();
        return rol;
    }

    public List<Rol> findById(String id) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesion = factory.getCurrentSession();
        sesion.getTransaction().begin();
        String hql = "FROM Rol rol WHERE rol.id =:id";
        Query<Rol> query = sesion.createQuery(hql);
        query.setParameter("id", id);
        List<Rol> rol = query.getResultList();
        sesion.close();
        return rol;
    }

    public List<Rol> findByNombre(String nombre) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesion = factory.getCurrentSession();
        sesion.getTransaction().begin();
        String hql = "FROM Rol rol WHERE rol.nombre =:nombre";
        Query<Rol> query = sesion.createQuery(hql);
        query.setParameter("nombre", nombre);
        List<Rol> rol = query.getResultList();
        sesion.close();
        return rol;
    }

    public void insertarRol(Rol rol) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesion = factory.getCurrentSession();
        sesion.getTransaction().begin();
        sesion.persist(rol);
        sesion.getTransaction().commit();
    }

    public void asignarRolUsuario(Rol rol, User user) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Session sesion = factory.getCurrentSession();
        sesion.getTransaction().begin();
        rol = findByNombre(rol.getNombre()).get(0);
        UserBD control = new UserBD();
        user = control.findByNombre(user.getNombre()).get(0);
        user.addRol(rol);
        sesion.persist(user);
        

    }
}
