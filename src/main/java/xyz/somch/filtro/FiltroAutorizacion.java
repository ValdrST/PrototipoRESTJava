/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.somch.filtro;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import static javax.ws.rs.core.Response.Status.OK;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import xyz.somch.jwt.TokenSecurity;
import static xyz.somch.jwt.TokenSecurity.getClaimsJwtToken;
import xyz.somch.model.Rol;
import xyz.somch.model.User;
import xyz.somch.model.UserBD;
import xyz.somch.prototiporestjava.ConstructorResponse;

/**
 *
 * @author dark_
 */
public class FiltroAutorizacion implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    public static final String HEADER_PROPERTY_ID = "id";
    public static final String AUTHORIZATION_PROPERTY = "Bearer";

    private static final String ACCESS_REFRESH = "Refresh Token";
    private static final String ACCESS_INVALID_TOKEN = "token invalido!";
    private static final String ACCESS_DENIED = "no puedes ver esto prro!";
    private static final String ACCESS_FORBIDDEN = "Acceso prohibido!";
    private static final String ACCESS_NO_SESSION = "sesion no iniciada";
    private static final String INTERNAL_ERROR = "error interno de servidor";

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        UserBD usuario;
        try {
            Method method = resourceInfo.getResourceMethod();
            if (!method.isAnnotationPresent(PermitAll.class)) {
                if (method.isAnnotationPresent(DenyAll.class)) {
                    requestContext.abortWith(ConstructorResponse.createResponse(Response.Status.FORBIDDEN, ACCESS_FORBIDDEN));
                    return;
                }
                final MultivaluedMap<String, String> headers = requestContext.getHeaders();
                final List<String> authProperty = headers.get(AUTHORIZATION_PROPERTY);
                if (authProperty == null || authProperty.isEmpty()) {
                    System.out.println("No hay token");
                    requestContext.abortWith(ConstructorResponse.createResponse(Response.Status.FORBIDDEN, ACCESS_DENIED));
                    return;
                }
                String jwt = authProperty.get(0);
                if (jwt.equals("")) {
                    requestContext.abortWith(ConstructorResponse.createResponse(Response.Status.FORBIDDEN, ACCESS_NO_SESSION));
                    return;
                }
                usuario = new UserBD();
                String id = getClaimsJwtToken(jwt);
                System.out.println("iD; "+ id + "jwt: " + jwt);
                User userLogin = usuario.findByID(id).get(0);
                String password = userLogin.getPassword();
                if (!userLogin.getSesion()) {
                    requestContext.abortWith(ConstructorResponse.createResponse(Response.Status.UNAUTHORIZED, ACCESS_NO_SESSION));
                    return;
                }
                TokenSecurity.validateJwtToken(jwt, userLogin.getPassword());
                if (method.isAnnotationPresent(RolesAllowed.class)) {
                    RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                    Set<String> rolesSet = new HashSet<>(Arrays.asList(rolesAnnotation.value()));
                    if (!isUserAllowed(userLogin.getRol(), rolesSet)) {
                        requestContext.abortWith(ConstructorResponse.createResponse(Response.Status.FORBIDDEN, ACCESS_DENIED));
                        return;
                    }
                }
                List<String> idList = new ArrayList<>();
                idList.add(id);
                headers.put(HEADER_PROPERTY_ID, idList);
            }
        } catch (InvalidJwtException ex) {
            try {
                System.out.println("Refresh token");
                usuario = new UserBD();
                String id = (String) ex.getJwtContext().getJwtClaims().getSubject();
                User user = usuario.findByID(id).get(0);
                if (user.getSesion() && ex.hasExpired()) {
                    try {
                        String jwt = ex.getJwtContext().getJwt();
                        if (user.getToken().equals(jwt)) {
                            jwt = TokenSecurity.refreshJwtToken(user, ex.getJwtContext().getJwtClaims().getExpirationTime());
                            user.setToken(jwt);
                            user.setRefreshToken((new SimpleDateFormat("HHmmssddMMyyyy")).format((new Date())));
                            if (usuario.actualizarUsuario(user, user)) {
                                Response response = ConstructorResponse.createResponse(Response.Status.OK, ACCESS_REFRESH);
                                response = Response.status(OK).header(AUTHORIZATION_PROPERTY,jwt).entity(response.getEntity()).build();
                                requestContext.abortWith(response);
                            } else {
                                requestContext.abortWith(ConstructorResponse.createResponse(Response.Status.INTERNAL_SERVER_ERROR, INTERNAL_ERROR));
                            }
                        }else {
                            requestContext.abortWith(ConstructorResponse.createResponse(Response.Status.FORBIDDEN, ACCESS_INVALID_TOKEN));
                        }
                    } catch (Exception ex1) {
                        requestContext.abortWith(ConstructorResponse.createResponse(Response.Status.INTERNAL_SERVER_ERROR, INTERNAL_ERROR));
                    }
                }
            } catch (MalformedClaimException ex1) {
                requestContext.abortWith(ConstructorResponse.createResponse(Response.Status.INTERNAL_SERVER_ERROR, INTERNAL_ERROR));
            }
        } catch (UnsupportedEncodingException ex) {
            requestContext.abortWith(ConstructorResponse.createResponse(Response.Status.INTERNAL_SERVER_ERROR, INTERNAL_ERROR));
        } catch (MalformedClaimException ex) {
            requestContext.abortWith(ConstructorResponse.createResponse(Response.Status.INTERNAL_SERVER_ERROR, INTERNAL_ERROR));
        }
    }

    private boolean isUserAllowed(final List<Rol> userRole, final Set<String> rolesSet) {
        boolean isAllowed = false;
        System.out.println("ROLES: "+userRole.toString() + " "+ rolesSet.toString());
        if (rolesSet.contains(userRole.get(0).getNombre())) {
            isAllowed = true;
            System.out.println("VERDAD");
        }
        return isAllowed;
    }
}
