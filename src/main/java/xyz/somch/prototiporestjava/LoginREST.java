/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.somch.prototiporestjava;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
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
    public Response autenticarUsuario(User user){
        try{
            UserBD controlador = new UserBD();
            autenticarUsuario(user);
            user = controlador.findByNombre(user.getNombre()).get(0);
            String token = generarJwt(user);
            Map<String,Object> map = new HashMap<String,Object>();
            controlador.setSesion(user, true);
            map.put( FiltroAutorizacion.AUTHORIZATION_PROPERTY, token );
            return ConstructorResponse.createResponse( Response.Status.OK, map );
        }catch (SecurityException se){
            return ConstructorResponse.createResponse(Response.Status.ACCEPTED , "Usuario o contraseña invalidos");
        }catch(Exception e){
            return ConstructorResponse.createResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}