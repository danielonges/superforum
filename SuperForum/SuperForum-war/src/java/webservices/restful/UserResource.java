/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservices.restful;

import entity.UserEntity;
import enumeration.UserType;
import exception.UnauthorisedRequestException;
import exception.UserExistsException;
import exception.UserNotFoundException;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import session.UserSessionBeanLocal;

/**
 *
 * @author danielonges
 */
@Path("user")
public class UserResource {

    @EJB
    private UserSessionBeanLocal userSessionBeanLocal;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/json")
    public Response register(UserEntity user) {
        user.setDateJoined(new Date());
        user.setUserType(UserType.NORMAL);
        try {
            userSessionBeanLocal.createUser(user);
            return Response.ok(user, MediaType.APPLICATION_JSON).build();
        } catch (UserExistsException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", e.getMessage())
                    .build();
            return Response.status(400).entity(exception).build();
        }
    }
    
    @PUT
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("userId") Long userId, UserEntity user) {
        user.setId(userId);
        try {
            userSessionBeanLocal.updateUser(user);
            return Response.status(204).build();
        } catch (UserNotFoundException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Not found")
                    .build();
            return Response.status(404).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UserEntity> getUsers(@QueryParam("username") String username) {
        if (username == null || username.equals("")) {
            return userSessionBeanLocal.searchUsers(null);
        } else {
            return userSessionBeanLocal.searchUsers(username);
        }
    }
    
    @PUT
    @Path("/block")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response blockUser(UserEntity user, @QueryParam("block") boolean block, @HeaderParam("adminId") Long adminId) {
        try {
            userSessionBeanLocal.blockUser(user, adminId, block);
            return Response.status(204).build();
        } catch (UnauthorisedRequestException e) {
             JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Unauthorised")
                    .build();
            return Response.status(401).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        } catch (UserNotFoundException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Not found")
                    .build();
            return Response.status(404).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }
}
