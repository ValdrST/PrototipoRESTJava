/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.somch.prototiporestjava;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;
import xyz.somch.filtro.FiltroAutorizacion;
import static xyz.somch.jwt.TokenSecurity.generarJwt;
import xyz.somch.model.User;
import xyz.somch.model.UserBD;
import xyz.somch.login.Login;

/**
 *
 * @author dark_
 */
@DeclareRoles({"admin", "user", "guest"})
@Path("/user")
public class LoginREST extends ResourceConfig {

    @POST
    @PermitAll
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response autenticarUsuario(User user) {
        try {
            UserBD controlador = new UserBD();

            Login.autenticarUsuario(user);
            System.out.println(user.toString());
            user = controlador.findByNombre(user.getNombre()).get(0);
            String token = generarJwt(user);
            Map<String, Object> map = new HashMap();
            controlador.setSesion(user, true);
            map.put(FiltroAutorizacion.AUTHORIZATION_PROPERTY, token);
            return ConstructorResponse.createResponse(Response.Status.FOUND, map);
        } catch (SecurityException se) {
            return ConstructorResponse.createResponse(Response.Status.OK, "Usuario o contraseña invalidos");
        } catch (Exception e) {
            return ConstructorResponse.createResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GET
    @RolesAllowed({"admin", "user"})
    @Path("/getAllUsers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerUsuarios() {
        try {
            UserBD controlador = new UserBD();
           
            System.out.println(controlador.findAll().toString());
            return ConstructorResponse.createResponse(Response.Status.OK, (List)controlador.findAll());
        } catch (SecurityException se) {
            return ConstructorResponse.createResponse(Response.Status.OK, "Usuario o contraseña invalidos");
        } catch (Exception e) {
            return ConstructorResponse.createResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    @POST
    @PermitAll
    @Path("/registro")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrarUsuario(User usuario){
        try{
            usuario.setRol("user");
            usuario.setUIID();
            UserBD controlador = new UserBD();
            if(controlador.insertarUsuario(usuario)){
                return ConstructorResponse.createResponse(Response.Status.FOUND, "Usuario creado correctamente");
            }else
                return ConstructorResponse.createResponse(Response.Status.OK,"Error en la creacion del usuario");
        }catch (Exception e) {
            return ConstructorResponse.createResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    @DELETE
    @RolesAllowed({"admin", "user"})
    @Path("/borrar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response borrarUsuario(User usuario){
        try{
            UserBD controlador = new UserBD();
            if(controlador.eliminarUsuario(usuario)){
                return ConstructorResponse.createResponse(Response.Status.FOUND, "usuario borrado con exito");
            }else
                return ConstructorResponse.createResponse(Response.Status.OK, "Error en el borrado del usuario");
        }catch (Exception e) {
            return ConstructorResponse.createResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
}
