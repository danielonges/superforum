/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservices.restful;

import entity.UserEntity;
import exception.UserNotFoundException;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import session.UserSessionBeanLocal;

/**
 *
 * @author danielonges
 */
@Path("auth")
public class AuthenticationResource {

    @EJB
    private UserSessionBeanLocal userSessionBeanLocal;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    // might need to change consumption type eventually
    @Consumes("application/json")
    public Response login(UserEntity userIn) {
        try {
            UserEntity user = userSessionBeanLocal.getUserByUsername(userIn.getUsername());
            if (!userIn.getPassword().equals(user.getPassword())) {
                JsonObject exception = Json.createObjectBuilder()
                        .add("error", "Incorrect password")
                        .build();
                return Response.status(401).entity(exception).build();
            }
            
            if (user.isIsBlocked()) {
                JsonObject exception = Json.createObjectBuilder()
                        .add("error", "User is blocked")
                        .build();
                return Response.status(401).entity(exception).build();
            }
            return Response.status(200).entity(new GenericEntity<UserEntity>(user){}).build();
        } catch (UserNotFoundException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", e.getMessage())
                    .build();
            return Response.status(404).entity(exception).build();
        }
    }
    
}
