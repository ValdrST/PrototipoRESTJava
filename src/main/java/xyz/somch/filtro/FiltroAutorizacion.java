/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xyz.somch.filtro;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.lang.JoseException;
import xyz.somch.jwt.TokenSecurity;
import static xyz.somch.jwt.TokenSecurity.getClaimsJwtToken;
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

    private static final String ACCESS_REFRESH = "refrescar token";
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
                    requestContext.abortWith(ConstructorResponse.createResponse(Response.Status.UNAUTHORIZED, ACCESS_DENIED));
                    return;
                }
                String jwt = authProperty.get(0);
                if (jwt.equals("")) {
                    requestContext.abortWith(ConstructorResponse.createResponse(Response.Status.UNAUTHORIZED, ACCESS_NO_SESSION));
                    return;
                }
                usuario = new UserBD();
                String id = getClaimsJwtToken(jwt);
                User userLogin = usuario.findByID(id).get(0);
                String password = userLogin.getPassword();
                if (!usuario.getSesion(userLogin)) {
                    requestContext.abortWith(ConstructorResponse.createResponse(Response.Status.UNAUTHORIZED, ACCESS_NO_SESSION));
                    return;
                }
                TokenSecurity.validateJwtToken(jwt, userLogin.getPassword());
                if (method.isAnnotationPresent(RolesAllowed.class)) {
                    RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                    Set<String> rolesSet = new HashSet<>(Arrays.asList(rolesAnnotation.value()));
                    if (!isUserAllowed(userLogin.getRol(), rolesSet)) {
                        requestContext.abortWith(ConstructorResponse.createResponse(Response.Status.UNAUTHORIZED, ACCESS_DENIED));
                        return;
                    }
                }
                List<String> idList = new ArrayList<>();
                idList.add(id);
                headers.put(HEADER_PROPERTY_ID, idList);
            }
        } catch (InvalidJwtException ex) {
            usuario = new UserBD();
            String id = (String) ex.getJwtContext().getJwtClaims().getClaimValue("id");
            User user = usuario.findByID(id).get(0);
            if (usuario.getSesion(usuario.findByID(id).get(0)) && ex.hasExpired()) {
                try {
                    String jwt = TokenSecurity.refreshJwtToken(user);
                    Map<String, Object> bearer = new HashMap<String, Object>();
                    bearer.put(AUTHORIZATION_PROPERTY, jwt);
                    requestContext.abortWith(ConstructorResponse.createResponse(Response.Status.OK, bearer));
                    return;
                } catch (JoseException ex1) {
                    requestContext.abortWith(ConstructorResponse.createResponse(Response.Status.INTERNAL_SERVER_ERROR, INTERNAL_ERROR));
                } catch (Exception ex1) {
                    requestContext.abortWith(ConstructorResponse.createResponse(Response.Status.INTERNAL_SERVER_ERROR, INTERNAL_ERROR));
                }
            }
        }
    }

    private boolean isUserAllowed(final String userRole, final Set<String> rolesSet) {
        boolean isAllowed = false;
        System.out.println("los roles" + rolesSet.toString());
        System.out.println("rol" + userRole);
        if (rolesSet.contains(userRole)) {
            isAllowed = true;
        }
        return isAllowed;
    }
}