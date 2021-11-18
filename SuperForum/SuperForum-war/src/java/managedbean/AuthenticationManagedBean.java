/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.UserEntity;
import enumeration.UserType;
import exception.UserNotFoundException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import session.UserSessionBeanLocal;

/**
 *
 * @author danielonges
 */
@Named(value = "authenticationManagedBean")
@SessionScoped
public class AuthenticationManagedBean implements Serializable {

    @EJB
    private UserSessionBeanLocal userSessionBeanLocal;

    private String username = null;
    private String password = null;
    private UserType userType = null;
    private long userId = -1;

    /**
     * Creates a new instance of AuthenticationManagedBean
     */
    public AuthenticationManagedBean() {

    }

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            UserEntity user = userSessionBeanLocal.getUserByUsername(username);
            if (user.getPassword().equals(password)) {
                if (user.isIsBlocked()) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "You have been blocked; unable to login."));
                    return "login.xhtml";
                }
                this.username = user.getUsername();
                this.password = user.getPassword();
                this.userType = user.getUserType();
                this.userId = user.getId();
                return "/index.xhtml?faces-redirect=true";
            } else {
                username = null;
                password = null;
                userId = -1;
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Incorrect password!"));
                return "login.xhtml";
            }
        } catch (UserNotFoundException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
            return "login.xhtml";
        }
    }
    
    public String logout() {
        username = null;
        password = null;
        userId = -1;

        return "/login.xhtml?faces-redirect=true";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    // helper function for conditional renders
    public boolean authenticateRequest(Long ownerId) {
        if (ownerId != null && this.userId == ownerId) {
            return true;
        }
        return this.userType == UserType.ADMIN;
    }
}
