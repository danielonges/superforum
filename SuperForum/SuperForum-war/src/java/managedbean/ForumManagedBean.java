/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.Forum;
import entity.ForumCategory;
import entity.ForumThread;
import exception.ForumCategoryExistsException;
import exception.ForumNotFoundException;
import exception.UnauthorisedRequestException;
import exception.UserNotFoundException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.faces.context.FacesContext;
import session.ForumSessionBeanLocal;
import session.UserSessionBeanLocal;

/**
 *
 * @author danielonges
 */
@Named(value = "forumManagedBean")
@ViewScoped
public class ForumManagedBean implements Serializable {

    private static final int POPULAR_THREAD_LIMIT = 3;

    private List<Forum> forums;
    private List<ForumCategory> allForumCategories;

    private String searchField;

    private Long currentForumId;
    private Forum currentForum;
    private List<ForumThread> popularForumThreads;
    
    private Long adminId;

    private Long ownerId;
    private Date dateCreated;
    private String forumCategory;
    private String title;
    private String description;

    @EJB
    private ForumSessionBeanLocal forumSessionBeanLocal;
    @EJB
    private UserSessionBeanLocal userSessionBeanLocal;

    /**
     * Creates a new instance of ForumManagedBean
     */
    public ForumManagedBean() {
    }

    @PostConstruct
    public void init() {
        if (searchField == null || searchField.equals("")) {
            this.forums = forumSessionBeanLocal.searchForumsByTitle(null);
        } else {
            this.forums = forumSessionBeanLocal.searchForumsByTitle(searchField);
        }
        
        this.allForumCategories = forumSessionBeanLocal.getAllForumCategories();
    }

    public void loadCurrentForum() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            this.currentForum = forumSessionBeanLocal.getForum(currentForumId);
            List<ForumThread> forumThreads = currentForum.getForumThreads();
            forumThreads.sort((ForumThread ft1, ForumThread ft2) -> ft2.getViews() - ft1.getViews());
            if (forumThreads.size() < POPULAR_THREAD_LIMIT) {
                popularForumThreads = forumThreads;
            } else {
                popularForumThreads = forumThreads.subList(0, POPULAR_THREAD_LIMIT);
            }

        } catch (ForumNotFoundException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }
    
    public void loadSearchedForums() {
        init();
    }
    
    public void loadForumDetails() {
        this.ownerId = currentForum.getOwner().getId();
        this.dateCreated = currentForum.getDateCreated();
        this.forumCategory = currentForum.getForumCategory().getCategory();
        this.title = currentForum.getTitle();
        this.description = currentForum.getDescription();
    }

    public Forum getForumFromForumThread(ForumThread ft) {
        try {
            return forumSessionBeanLocal.getForumByForumThread(ft);
        } catch (ForumNotFoundException e) {
            return null;
        }
    }
    
    public String searchForums() {
        return "/forum/all-forums.xhtml?searchParam=" + searchField + "&faces-redirect=true";
    }

    public String createForum() {
        FacesContext context = FacesContext.getCurrentInstance();
        Forum forum = new Forum();
        try {
            forum.setTitle(title);
            forum.setDescription(description);
            forum.setDateCreated(new Date());
            forum.setOwner(userSessionBeanLocal.getUser(ownerId));
            forum.setForumCategory(new ForumCategory(forumCategory));
            forumSessionBeanLocal.createForum(forum, ownerId);
            init();
            return "forum.xhtml?fId=" + forum.getId() + "&faces-redirect=true";
        } catch (UserNotFoundException | UnauthorisedRequestException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
            e.printStackTrace();
            return "new-forum.xhtml";
        }
    }

    public String updateForum() {
        FacesContext context = FacesContext.getCurrentInstance();
        currentForum.setTitle(title);
        currentForum.setDescription(description);
        currentForum.setForumCategory(new ForumCategory(forumCategory));
        try {
            forumSessionBeanLocal.updateForum(currentForum, adminId);
        } catch (ForumNotFoundException | UnauthorisedRequestException | UserNotFoundException e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
        return "forum.xhtml?fId=" + currentForumId + "&faces-redirect=true";
    }
    
    public String deleteForum() {
        FacesContext context = FacesContext.getCurrentInstance();
        forumCategory = currentForum.getForumCategory().getCategory();
        try {
            forumSessionBeanLocal.deleteForum(currentForum, adminId);
        } catch (ForumNotFoundException | UnauthorisedRequestException | UserNotFoundException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
        return "forum-category.xhtml?forum-category=" + forumCategory + "&faces-redirect=true";
    }

    public void createForumCategory() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            forumSessionBeanLocal.createForumCategory(forumCategory);
            init();
        } catch (ForumCategoryExistsException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public List<Forum> getForums() {
        return forums;
    }

    public void setForums(List<Forum> forums) {
        this.forums = forums;
    }

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public Forum getCurrentForum() {
        return currentForum;
    }

    public void setCurrentForum(Forum currentForum) {
        this.currentForum = currentForum;
    }

    public Long getCurrentForumId() {
        return currentForumId;
    }

    public void setCurrentForumId(Long currentForumId) {
        this.currentForumId = currentForumId;
    }

    public List<ForumThread> getPopularForumThreads() {
        return popularForumThreads;
    }

    public void setPopularForumThreads(List<ForumThread> popularForumThreads) {
        this.popularForumThreads = popularForumThreads;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getForumCategory() {
        return forumCategory;
    }

    public void setForumCategory(String forumCategory) {
        this.forumCategory = forumCategory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ForumCategory> getAllForumCategories() {
        return allForumCategories;
    }

    public void setAllForumCategories(List<ForumCategory> allForumCategories) {
        this.allForumCategories = allForumCategories;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

}
