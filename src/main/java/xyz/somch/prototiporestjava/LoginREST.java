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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import static javax.ws.rs.core.Response.Status.FOUND;
import org.glassfish.jersey.server.ResourceConfig;
import xyz.somch.filtro.FiltroAutorizacion;
import static xyz.somch.filtro.FiltroAutorizacion.AUTHORIZATION_PROPERTY;
import static xyz.somch.jwt.TokenSecurity.generarJwt;
import static xyz.somch.jwt.TokenSecurity.getClaimsJwtToken;
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

    @Context
    private ContainerRequestContext requestContext;

    @POST
    @PermitAll
    @Path("/login")
    @Consumes("application/json")
    @Produces("application/json")
    public Response autenticarUsuario(User user) {
        try {
            UserBD controlador = new UserBD();
            Login.autenticarUsuario(user);
            System.out.println(user.toString());
            user = controlador.findByNombre(user.getNombre()).get(0);
            String token = generarJwt(user);
            user.setToken(token);
            user.setSesion(true);
            if (controlador.actualizarUsuario(user, user)) {
                Response response = ConstructorResponse.createResponse(Response.Status.FOUND, "sesion iniciada con exito");
                return Response.status(FOUND).header(AUTHORIZATION_PROPERTY,token).entity(response.getEntity()).build();
            } else {
                return ConstructorResponse.createResponse(Response.Status.INTERNAL_SERVER_ERROR, "error en la autenticacion");
            }
        } catch (SecurityException se) {
            return ConstructorResponse.createResponse(Response.Status.OK, "Usuario o contraseña invalidos");
        } catch (Exception e) {
            return ConstructorResponse.createResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GET
    @RolesAllowed({"admin", "user"})
    @Path("/getAllUsers")
    @Consumes("application/json")
    @Produces("application/json")
    public Response obtenerUsuarios() {
        try {
            UserBD controlador = new UserBD();
            System.out.println(controlador.findAll().toString());
            return ConstructorResponse.createResponse(Response.Status.OK, (List) controlador.findAll());
        } catch (SecurityException se) {
            return ConstructorResponse.createResponse(Response.Status.OK, "Usuario o contraseña invalidos");
        } catch (Exception e) {
            return ConstructorResponse.createResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @POST
    @PermitAll
    @Path("/registro")
    @Consumes("application/json")
    @Produces("application/json")
    public Response registrarUsuario(User usuario) {
        try {
            usuario.setUIID();
            UserBD controlador = new UserBD();
            if (controlador.insertarUsuario(usuario)) {
                return ConstructorResponse.createResponse(Response.Status.FOUND, "Usuario creado correctamente");
            } else {
                return ConstructorResponse.createResponse(Response.Status.OK, "Error en la creacion del usuario");
            }
        } catch (Exception e) {
            return ConstructorResponse.createResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DELETE
    @RolesAllowed({"admin", "user"})
    @Path("/borrar")
    @Consumes("application/json")
    @Produces("application/json")
    public Response borrarUsuario(User usuario) {
        try {
            UserBD controlador = new UserBD();
            if (controlador.eliminarUsuario(usuario)) {
                return ConstructorResponse.createResponse(Response.Status.FOUND, "usuario borrado con exito");
            } else {
                return ConstructorResponse.createResponse(Response.Status.OK, "Error en el borrado del usuario");
            }
        } catch (Exception e) {
            return ConstructorResponse.createResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DELETE
    @RolesAllowed({"admin", "user"})
    @Path("/logout")
    @Consumes("application/json")
    @Produces("application/json")
    public Response cerrarSesion(User usuario) {
        try {
            System.out.println("Cerrar sesion");
            UserBD controlador = new UserBD();
            usuario = controlador.findByNombre(usuario.getNombre()).get(0);
            usuario.setSesion(false);
            usuario.setToken("");
            usuario.setRefreshToken("");
            if (controlador.actualizarUsuario(usuario, usuario)) {
                return ConstructorResponse.createResponse(Response.Status.FOUND, "sesion cerrada con exito");
            } else {
                return ConstructorResponse.createResponse(Response.Status.OK, "error en cerrar sesion");
            }
        } catch (Exception e) {
            return ConstructorResponse.createResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PUT
    @RolesAllowed({"admin", "user"})
    @Path("/modificar/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response modificarUsuario(@PathParam("id") String id, User usuario) {
        try {
            final MultivaluedMap<String, String> headers = requestContext.getHeaders();
            final List<String> authProperty = headers.get(FiltroAutorizacion.AUTHORIZATION_PROPERTY);
            UserBD controlador = new UserBD();
            System.out.println(getClaimsJwtToken(authProperty.get(0)));
            if (id.equals(getClaimsJwtToken(authProperty.get(0)))) {
                User newUsuario = controlador.findByID(id).get(0);
                User oldUsuario = newUsuario;
                oldUsuario.setNombre(usuario.getNombre());
                oldUsuario.setPassword(usuario.getPassword());
                if (controlador.actualizarUsuario(oldUsuario, newUsuario)) {
                    return ConstructorResponse.createResponse(Response.Status.FOUND, "Datos actualizados correctamente");
                } else {
                    return ConstructorResponse.createResponse(Response.Status.OK, "Error en la actualizacion de los datos");
                }
            }else {
                return ConstructorResponse.createResponse(Response.Status.OK, "Usuario no autorizado a realizar cambios");
            }

        } catch (Exception e) {
            return ConstructorResponse.createResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
