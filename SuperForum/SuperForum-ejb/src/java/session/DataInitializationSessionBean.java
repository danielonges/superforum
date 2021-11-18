/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.Forum;
import entity.ForumCategory;
import entity.ForumThread;
import entity.Post;
import entity.UserEntity;
import enumeration.UserType;
import exception.ForumNotFoundException;
import exception.ForumThreadNotFoundException;
import exception.UnauthorisedRequestException;
import exception.UserExistsException;
import exception.UserNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;

/**
 *
 * @author danielonges
 */
@Singleton
@LocalBean
@Startup

public class DataInitializationSessionBean {

    @EJB
    private UserSessionBeanLocal userSessionBeanLocal;
    @EJB
    private ForumSessionBeanLocal forumSessionBeanLocal;

    public DataInitializationSessionBean() {
    }

    @PostConstruct
    private void init() {
        try {
            userSessionBeanLocal.getUserByUsername("user");
        } catch (UserNotFoundException e) {
            initialiseData();
        }
    }

    private void initialiseData() {
        UserEntity user = new UserEntity();
        user.setUsername("user");
        user.setPassword("password");
        user.setEmail("user@email.com");
        user.setBio("hello!");
        user.setDateJoined(new Date());
        user.setUserType(UserType.NORMAL);
        user.setIsBlocked(false);
        try {
            userSessionBeanLocal.createUser(user);
        } catch (UserExistsException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        UserEntity admin = new UserEntity();
        admin.setUsername("admin");
        admin.setPassword("password");
        admin.setEmail("admin@email.com");
        admin.setBio("hello!");
        admin.setDateJoined(new Date());
        admin.setUserType(UserType.ADMIN);
        admin.setIsBlocked(false);
        try {
            userSessionBeanLocal.createUser(admin);
        } catch (UserExistsException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        Forum forum1 = new Forum();
        forum1.setOwner(admin);
        forum1.setDateCreated(new Date());
        forum1.setForumCategory(new ForumCategory("Lifestyle"));
        forum1.setTitle("Test Forum 1");
        forum1.setDescription("Hello!");
        try {
            forumSessionBeanLocal.createForum(forum1, admin.getId());
        } catch (UnauthorisedRequestException | UserNotFoundException e) {
            e.printStackTrace();
        }

        Forum forum2 = new Forum();
        forum2.setOwner(admin);
        forum2.setDateCreated(new Date());
        forum2.setForumCategory(new ForumCategory("Technology"));
        forum2.setTitle("Test Forum 2");
        forum2.setDescription("Byebye!");
        try {
            forumSessionBeanLocal.createForum(forum2, admin.getId());
        } catch (UnauthorisedRequestException | UserNotFoundException e) {
            e.printStackTrace();
        }

        ForumThread ft = new ForumThread();
        ft.setTitle("Thread 1");
        ft.setOwner(user);
        ft.setViews(0);
        ft.setDateCreated(new Date());
        ft.setIsClosed(false);
        ft.setPosts(new ArrayList<>());
        try {
            forumSessionBeanLocal.createForumThread(forum1.getId(), ft);
        } catch (ForumNotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 1; i <= 8; i++) {
            Post post = new Post();
            if (i % 2 == 0) {
                post.setOwner(admin);
            } else {
                post.setOwner(user);
            }
            post.setDateCreated(new Date());
            post.setContent("This is post " + i + " by " + post.getOwner().getUsername() + "!");
            try {
                forumSessionBeanLocal.createPost(post, ft);
            } catch (ForumThreadNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
