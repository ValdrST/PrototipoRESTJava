/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.somch.db;

/**
 *
 * @author dark_
 */
public class ConstantesBD {
    public static String dbClass = "com.mysql.jdbc.Driver";
    private static final String dbName= "usuarios";
    public static final String dbUrl = "jdbc:mysql://localhost:3306/"+dbName+"?useServerPrepStmts=true";
    public static String dbUser = "valdr";
    public static String dbPwd = "nomad123";
}
