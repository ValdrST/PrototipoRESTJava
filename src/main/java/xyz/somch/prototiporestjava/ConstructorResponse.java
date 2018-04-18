package xyz.somch.prototiporestjava;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import static xyz.somch.filtro.FiltroAutorizacion.AUTHORIZATION_PROPERTY;

import xyz.somch.utilidades.JsonSerializable;

public class ConstructorResponse {

    public static Response createResponse(Response.Status status) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("message", status.toString());
        } catch (JSONException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(status).entity(jsonObject.toString()).build();
    }

    public static Response createResponse(Response.Status status, String message) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("message", message);
        } catch (JSONException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(status).entity(jsonObject.toString()).build();
    }

    public static Response createResponse(Response.Status status, JsonSerializable json) throws JSONException {
        return Response.status(status).entity(json.toJson().toString()).build();
    }

    public static Response createResponse(Response.Status status, List<JsonSerializable> json) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        System.out.println(json.toString());
        for (int i = 0; i < json.size(); i++) {
            jsonArray.put(json.get(i).toJson());
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("usuario", jsonArray);

        return Response.status(status).entity(jsonObject.toString()).build();
    }

    public static Response createResponse(Response.Status status, Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject();

        try {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }
        } catch (JSONException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(status).entity(jsonObject.toString()).build();
    }

    public static Response createResponse(Response.Status status, String jwt, String message) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("message", message);
        } catch (JSONException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        
        return Response.status(status).header(AUTHORIZATION_PROPERTY,jwt).entity(jsonObject.toString()).build();
    }

    /*public static Response createResponse( Response.Status status, List<Object> list) {
            JSONArray jsonArray = new JSONArray();
            for(int i = 0; i<list.size(); i++){
                jsonArray.put(list.get(i).toJson());
            }
        }*/
}
