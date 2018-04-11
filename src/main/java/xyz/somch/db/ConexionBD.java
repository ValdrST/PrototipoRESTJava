/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.somch.db;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
/**
 *
 * @author dark_
 */
public class ConexionBD {
    public static SessionFactory crearConexion() {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        return factory;
    }
    
    public static void destruirConexion(SessionFactory factory){
        factory.close();
    }
}
