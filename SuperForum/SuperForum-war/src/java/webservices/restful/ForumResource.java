/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservices.restful;

import entity.Forum;
import entity.ForumCategory;
import entity.ForumThread;
import entity.Post;
import exception.CannotDeleteException;
import exception.ForumCategoryExistsException;
import exception.ForumCategoryNotFoundException;
import exception.ForumNotFoundException;
import exception.ForumThreadNotFoundException;
import exception.PostNotFoundException;
import exception.UnauthorisedRequestException;
import exception.UserNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import session.ForumSessionBeanLocal;
import session.UserSessionBeanLocal;

/**
 * REST Web Service
 *
 * @author danielonges
 */
@Path("forum")
public class ForumResource {

    @EJB
    private ForumSessionBeanLocal forumSessionBeanLocal;
    @EJB
    private UserSessionBeanLocal userSessionBeanLocal;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Forum> getAllForums() {
        return forumSessionBeanLocal.searchForumsByTitle(null);
    }

    @GET
    @Path("/title-query")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Forum> searchForums(@QueryParam("title") String title) {
        return forumSessionBeanLocal.searchForumsByTitle(title);
    }

    @GET
    @Path("/category-query")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchForumsByCategory(@QueryParam("category") String category) {
        try {
            ForumCategory forumCategory = forumSessionBeanLocal.getForumCategoryByName(category);
            List<Forum> forums = forumSessionBeanLocal.searchForumsByCategory(forumCategory);
            return Response.ok(new GenericEntity<List<Forum>>(forums){}, MediaType.APPLICATION_JSON).build();
        } catch (ForumCategoryNotFoundException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", e.getMessage())
                    .build();
            return Response.status(404).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("/{fId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getForum(@PathParam("fId") Long fId) {
        try {
            Forum forum = forumSessionBeanLocal.getForum(fId);
            return Response.ok().entity(forum).type(MediaType.APPLICATION_JSON).build();
        } catch (ForumNotFoundException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Not found")
                    .build();
            return Response.status(404).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createForum(Forum forum, @HeaderParam("adminId") Long adminId) {
        try {
            forum.setDateCreated(new Date());
            forum.setOwner(userSessionBeanLocal.getUser(adminId));
            forum.setForumThreads(new ArrayList<>());
            forumSessionBeanLocal.createForum(forum, adminId);
            return Response.ok().entity(forum).type(MediaType.APPLICATION_JSON).build();
        } catch (UnauthorisedRequestException | UserNotFoundException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Not found")
                    .build();
            return Response.status(404).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }

    @PUT
    @Path("/{fId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateFourm(@PathParam("fId") Long fId, Forum forum, @HeaderParam("adminId") Long adminId) {
        forum.setId(fId);
        try {
            forumSessionBeanLocal.updateForum(forum, adminId);
            return Response.status(204).build();
        } catch (ForumNotFoundException | UserNotFoundException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Not found")
                    .build();
            return Response.status(404).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        } catch (UnauthorisedRequestException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Unauthorised")
                    .build();
            return Response.status(401).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }

    @DELETE
    @Path("/{fId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteForum(@PathParam("fId") Long fId, @HeaderParam("adminId") Long adminId) {
        try {
            Forum forum = forumSessionBeanLocal.getForum(fId);
            forumSessionBeanLocal.deleteForum(forum, adminId);
            return Response.ok().build();
        } catch (ForumNotFoundException | UserNotFoundException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Not found")
                    .build();
            return Response.status(404).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        } catch (UnauthorisedRequestException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Unauthorised")
                    .build();
            return Response.status(401).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("/forumcategory")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ForumCategory> getForumCategories() {
        return forumSessionBeanLocal.getAllForumCategories();
    }

    @POST
    @Path("/forumcategory")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createForumCategory(ForumCategory forumCategory) {
        try {
            forumSessionBeanLocal.createForumCategory(forumCategory.getCategory());
            return Response.ok().build();
        } catch (ForumCategoryExistsException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", e.getMessage())
                    .build();
            return Response.status(409).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("/forumthread/{ftId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getForumThread(@PathParam("ftId") Long ftId) {
        try {
            ForumThread forumThread = forumSessionBeanLocal.getForumThread(ftId);
            return Response.ok().entity(forumThread).type(MediaType.APPLICATION_JSON).build();
        } catch (ForumThreadNotFoundException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Not found")
                    .build();
            return Response.status(404).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("/forumthread/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserForumThreads(@HeaderParam("userId") Long userId) {
        try {
            List<ForumThread> userForumThreads = forumSessionBeanLocal.getUserForumThreads(userId);
            return Response.ok(new GenericEntity<List<ForumThread>>(userForumThreads) {
            }, MediaType.APPLICATION_JSON).build();
        } catch (UserNotFoundException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Not found")
                    .build();
            return Response.status(404).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Path("/{fId}/forumthread")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createForumThread(@PathParam("fId") Long fId, ForumThread forumThread) {
        try {
            List<Post> posts = forumThread.getPosts();
            
            forumThread.setDateCreated(new Date());
            forumThread.setPosts(new ArrayList<>());
            forumThread.setDateCreated(new Date());
            forumThread.setViews(0);
            forumThread.setIsClosed(false);
            
            forumSessionBeanLocal.createForumThread(fId, forumThread);
            
            if (posts != null && posts.size() > 0) {
                for (Post post : posts) {
                    post.setDateCreated(new Date());
                    post.setOwner(forumThread.getOwner());
                    forumSessionBeanLocal.createPost(post, forumThread);
                }
                forumThread.setPosts(posts);
            } else {
                forumThread.setPosts(new ArrayList<>());
            }
            return Response.ok(forumThread, MediaType.APPLICATION_JSON).build();
        } catch (ForumNotFoundException | ForumThreadNotFoundException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Not found")
                    .build();
            return Response.status(404).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }

    @PUT
    @Path("/forumthread/{ftId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateForumThread(@PathParam("ftId") Long ftId, ForumThread forumThread, @HeaderParam("userId") Long userId) {
        forumThread.setId(ftId);
        try {
            forumSessionBeanLocal.updateForumThread(forumThread, userId);
            return Response.status(204).build();
        } catch (ForumThreadNotFoundException | UserNotFoundException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Not found")
                    .build();
            return Response.status(404).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        } catch (UnauthorisedRequestException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Unauthorised")
                    .build();
            return Response.status(401).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }
    
    @PUT
    @Path("/forumthread/view")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response incrementViewCount(ForumThread forumThread) {
        try {
            forumSessionBeanLocal.incrementThreadViewCount(forumThread.getId());
            return Response.status(204).build();
        } catch (ForumThreadNotFoundException e) {
             JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Not found")
                    .build();
            return Response.status(404).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }

    @DELETE
    @Path("/forumthread/{ftId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteForumThread(@PathParam("ftId") Long ftId, @HeaderParam("userId") Long userId) {
        try {
            ForumThread forumThread = forumSessionBeanLocal.getForumThread(ftId);
            forumSessionBeanLocal.deleteForumThread(forumThread, userId);
            return Response.status(200).build();
        } catch (ForumThreadNotFoundException | UserNotFoundException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Not found")
                    .build();
            return Response.status(404).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        } catch (UnauthorisedRequestException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Unauthorised")
                    .build();
            return Response.status(401).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        } catch (CannotDeleteException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", e.getMessage())
                    .build();
            return Response.status(405).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }
    
    @PUT
    @Path("/forumthread/close")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response closeForumThread(ForumThread forumThread, @HeaderParam("userId") Long userId, @QueryParam("closed") boolean closed) {
        try {
            forumSessionBeanLocal.closeForumThread(forumThread.getId(), userId, closed);
            return Response.status(204).build();
        } catch (UnauthorisedRequestException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Unauthorised")
                    .build();
            return Response.status(401).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        } catch (ForumThreadNotFoundException | UserNotFoundException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Not found")
                    .build();
            return Response.status(404).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Path("/forumthread/{ftId}/posts")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPost(Post post, @PathParam("ftId") Long ftId) {
        try {
            post.setDateCreated(new Date());
            ForumThread forumThread = forumSessionBeanLocal.getForumThread(ftId);
            forumSessionBeanLocal.createPost(post, forumThread);
            return Response.ok(post, MediaType.APPLICATION_JSON).build();
        } catch (ForumThreadNotFoundException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Not found")
                    .build();
            return Response.status(404).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }

    @PUT
    @Path("/posts/{pId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePost(@PathParam("pId") Long pId, Post post, @HeaderParam("userId") Long userId) {
        post.setId(pId);
        try {
            forumSessionBeanLocal.updatePost(post, userId);
            return Response.status(204).build();
        } catch (UserNotFoundException | PostNotFoundException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Not found")
                    .build();
            return Response.status(404).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        } catch (UnauthorisedRequestException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Unauthorised")
                    .build();
            return Response.status(401).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }

    @DELETE
    @Path("/posts/{pId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePost(@PathParam("pId") Long pId, @HeaderParam("userId") Long userId) {
        try {
            Post post = forumSessionBeanLocal.getPost(pId);
            forumSessionBeanLocal.deletePost(post, userId);
            return Response.ok().build();
        } catch (PostNotFoundException | UserNotFoundException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Not found")
                    .build();
            return Response.status(404).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        } catch (UnauthorisedRequestException e) {
            JsonObject exception = Json.createObjectBuilder()
                    .add("error", "Unauthorised")
                    .build();
            return Response.status(401).entity(exception)
                    .type(MediaType.APPLICATION_JSON).build();
        }
    }
}
