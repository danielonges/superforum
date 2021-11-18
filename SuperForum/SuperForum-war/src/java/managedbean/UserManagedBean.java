/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.UserEntity;
import enumeration.UserType;
import exception.UnauthorisedRequestException;
import exception.UserExistsException;
import exception.UserNotFoundException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import javax.inject.Named;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import session.UserSessionBeanLocal;

/**
 *
 * @author danielonges
 */
@Named(value = "userManagedBean")
@ViewScoped
public class UserManagedBean implements Serializable {

    private static final int PROFILE_PHOTO_WIDTH = 300;
    private static final int PROFILE_PHOTO_HEIGHT = 300;

    private List<UserEntity> users;
    private String userSearchField;

    private Long userId;
    private UserEntity user;

    private String username;
    private String password;
    private String email;
    private String bio;

    private UploadedFile file;

    @EJB
    private UserSessionBeanLocal userSessionBeanLocal;

    /**
     * Creates a new instance of UserManagedBean
     */
    public UserManagedBean() {
    }

    @PostConstruct
    public void init() {
        if (userSearchField == null || userSearchField.equals("")) {
            users = userSessionBeanLocal.searchUsers(null);
        } else {
            users = userSessionBeanLocal.searchUsers(userSearchField);
        }
    }

    public void handleSearch() {
        init();
    }

    public void loadUser() {
        try {
            user = userSessionBeanLocal.getUser(userId);
            username = user.getUsername();
            password = user.getPassword();
            email = user.getEmail();
            bio = user.getBio();
        } catch (UserNotFoundException e) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public String updateUser() {
        user.setUsername(username);
        user.setEmail(email);
        user.setBio(bio);
        try {
            userSessionBeanLocal.updateUser(user);
        } catch (UserNotFoundException e) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
        return String.format("user-profile.xhtml?userId=%d&faces-redirect=true", userId);
    }

    public String blockUser(Long userToBlockId, Long adminId, boolean toBlock) {
        try {
            userSessionBeanLocal.blockUser(userSessionBeanLocal.getUser(userToBlockId), adminId, toBlock);
        } catch (UnauthorisedRequestException | UserNotFoundException e) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
        return "manage-users.xhtml?faces-redirect=true";
    }

    public String register() {
        FacesContext context = FacesContext.getCurrentInstance();
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setBio(bio);
        user.setDateJoined(new Date());
        user.setUserType(UserType.NORMAL);
        user.setIsBlocked(false);
        try {
            userSessionBeanLocal.createUser(user);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User " + username + " has been succsessfully registered!"));
            return "/login.xhtml?faces-redirect=true";
        } catch (UserExistsException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
            return "";
        }

    }

    public void uploadProfilePhoto() {
        FacesContext context = FacesContext.getCurrentInstance();
        System.out.println("call me bby");
        byte[] contents = file.getContent(); // Or getInputStream()
        System.out.println(contents.toString());

        // make directory if it doesn't exist
        File storagePath = new File("./images");
        if (!storagePath.exists()) {
            storagePath.mkdir();
        }

        // check if image is 300px by 300px
        BufferedImage bi;
        try {
            bi = ImageIO.read(new ByteArrayInputStream(contents));
            int width = bi.getWidth();
            int height = bi.getHeight();
            System.out.println("Width: " + width + ", height: " + height);
            if (width != PROFILE_PHOTO_WIDTH || height != PROFILE_PHOTO_HEIGHT) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Image size must be 64px by 64px!"));
                return;
            }
        } catch (IOException ex) {
            Logger.getLogger(UserManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        Path path = Paths.get(String.format("./images/%s-id-%d.jpg", username, userId));
        try {
            Files.write(path, contents);
            System.out.println(path.toAbsolutePath().toString());
            System.out.println("yay!");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public StreamedContent retrieveImage() {
        Path path = Paths.get(String.format("./images/%s-id-%d.jpg", username, userId));
        try {
            byte[] content = Files.readAllBytes(path);
            return DefaultStreamedContent.builder().contentType("image/jpg").stream(() -> new ByteArrayInputStream(content)).build();
        } catch (IOException ex) {
            Logger.getLogger(UserManagedBean.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return new DefaultStreamedContent();
        }
    }

    public String getUserSearchField() {
        return userSearchField;
    }

    public void setUserSearchField(String userSearchField) {
        this.userSearchField = userSearchField;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

}
