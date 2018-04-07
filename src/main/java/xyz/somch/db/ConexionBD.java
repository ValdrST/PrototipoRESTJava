/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.somch.db;


import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
/**
 *
 * @author dark_
 */
public class ConexionBD {
    public static Connection crearConexion() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conexion;
        conexion = DriverManager.getConnection(ConstantesBD.dbUrl, ConstantesBD.dbUser, ConstantesBD.dbPwd);
        return conexion;
    }
    
    public static void destruirConexion(Connection conexion) throws SQLException {
        conexion.close();
    }
}
