/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.UserEntity;
import exception.UserNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import session.UserSessionBeanLocal;

/**
 *
 * @author danielonges
 */
@Named(value = "photoUtilManagedBean")
@ApplicationScoped
public class PhotoUtilManagedBean implements Serializable {

    private static final String IMAGE_DIRECTORY = "./images";
    private static final String IMAGE_NAME_FORMAT = "./images/%s-id-%d.jpg";

    @EJB
    private UserSessionBeanLocal userSessionBeanLocal;

    /**
     * Creates a new instance of PhotoUtilManagedBean
     */
    public PhotoUtilManagedBean() {
    }

    public void uploadProfilePhoto(FileUploadEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();

        UploadedFile uploadedFile = event.getFile();
        byte[] contents = uploadedFile.getContent(); // Or getInputStream()

        // make directory if it doesn't exist
        File storagePath = new File(IMAGE_DIRECTORY);
        if (!storagePath.exists()) {
            storagePath.mkdir();
        }
        
        String username = (String) event.getComponent().getAttributes().get("username");
        Long userId = (Long) event.getComponent().getAttributes().get("userId");


        Path path = Paths.get(String.format(IMAGE_NAME_FORMAT, username, userId));
        try {
            Files.write(path, contents);
            UserEntity user = userSessionBeanLocal.getUser(userId);
            user.setProfilePhoto(path.toString());
            userSessionBeanLocal.updateUser(user);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Profile photo successfully uploaded!"));
        } catch (IOException | UserNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public StreamedContent getProfilePhoto() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            return new DefaultStreamedContent();
        } else {
            String contextUsername = context.getExternalContext().getRequestParameterMap().get("username");
            Long contextUserId = Long.valueOf(context.getExternalContext().getRequestParameterMap().get("userId"));
            Path path = Paths.get(String.format(IMAGE_NAME_FORMAT, contextUsername, contextUserId));
            try {
                byte[] contents = Files.readAllBytes(path);
                return DefaultStreamedContent.builder().contentType("image/jpg").stream(() -> new ByteArrayInputStream(contents)).build();
            } catch (IOException e) {
                return new DefaultStreamedContent();
            }
        }
    }

    public void deletePhoto(String username, Long userId) {
        File file = new File(String.format(IMAGE_NAME_FORMAT, username, userId));
        file.delete();

        try {
            UserEntity user = userSessionBeanLocal.getUser(userId);
            user.setProfilePhoto(null);
            userSessionBeanLocal.updateUser(user);
        } catch (UserNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
