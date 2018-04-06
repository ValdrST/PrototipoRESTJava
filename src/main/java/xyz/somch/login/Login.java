/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.somch.login;

import xyz.somch.model.User;
import xyz.somch.model.UserBD;

/**
 *
 * @author dark_
 */
public class Login {
    public static void autenticarUsuario(User usuario) {
        UserBD usuarioBD = new UserBD();
        User usuarioSec = new User();
        usuarioSec = usuarioBD.findByNombre(usuario.getNombre()).get(0);
        if(!usuario.getPassword().equals(usuarioSec.getPassword())){
            throw new SecurityException("Usuario o contraseña invalidos");
        }
    }
}
