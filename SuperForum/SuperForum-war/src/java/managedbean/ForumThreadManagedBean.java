/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.Forum;
import entity.ForumThread;
import entity.Post;
import exception.CannotDeleteException;
import exception.ForumNotFoundException;
import exception.ForumThreadNotFoundException;
import exception.PostNotFoundException;
import exception.UnauthorisedRequestException;
import exception.UserNotFoundException;

import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import session.ForumSessionBeanLocal;
import session.UserSessionBeanLocal;

/**
 *
 * @author danielonges
 */
@Named(value = "forumThreadManagedBean")
@ViewScoped
public class ForumThreadManagedBean implements Serializable {

    @EJB
    private UserSessionBeanLocal userSessionBeanLocal;
    @EJB
    private ForumSessionBeanLocal forumSessionBeanLocal;

    private List<ForumThread> myRecentForumThreads;

    private Long forumId;
    private Forum forum;

    private Long userId;
    private String title;
    private List<Post> posts;
    private boolean isClosed;
    private Long closedById;
    
    private String firstPostContent;

    private Long currentThreadId;
    private ForumThread currentForumThread;

    private boolean showReplyEditor = false;
    private boolean showInPostEditor = false;

    private Long postToEditId;
    private String editPostContent;
    
    private String postContent;

    /**
     * Creates a new instance of ForumThreadManagedBean
     */
    public ForumThreadManagedBean() {
    }

    public void loadForum() {
        try {
            forum = forumSessionBeanLocal.getForum(forumId);
        } catch (ForumNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadCurrentForumThread() {
        try {
            currentForumThread = forumSessionBeanLocal.getForumThread(currentThreadId);
        } catch (ForumThreadNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void loadForumThreadDetails() {
        this.userId = currentForumThread.getOwner().getId();
        this.title = currentForumThread.getTitle();
        this.posts = currentForumThread.getPosts();
        this.isClosed = currentForumThread.isIsClosed();
        if (isClosed) {
            this.closedById = currentForumThread.getClosedBy().getId();
        }
    }

    public void loadMyRecentForums() {
        try {
            myRecentForumThreads = forumSessionBeanLocal.getUserForumThreads(userId);
        } catch (UserNotFoundException e) {
            myRecentForumThreads = new ArrayList<>();
        }
    }

    public String createForumThread() {
        FacesContext context = FacesContext.getCurrentInstance();

        ForumThread forumThread = new ForumThread();
        try {
            forumThread.setOwner(userSessionBeanLocal.getUser(userId));
            forumThread.setTitle(title);
            forumThread.setPosts(new ArrayList<>());
            forumThread.setDateCreated(new Date());
            forumThread.setViews(0);
            forumThread.setIsClosed(false);
            forumSessionBeanLocal.createForumThread(forumId, forumThread);
            if (firstPostContent != null && !firstPostContent.equals("")) {
                Post post = new Post();
                post.setOwner(userSessionBeanLocal.getUser(userId));
                post.setDateCreated(new Date());
                post.setContent(firstPostContent);
                forumSessionBeanLocal.createPost(post, forumThread);
            }
            // TODO: redirect to the actual thread page!
        } catch (UserNotFoundException | ForumNotFoundException | ForumThreadNotFoundException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
        return String.format("forum-thread.xhtml?threadId=%d&forumId=%d&faces-redirect=true", forumThread.getId(), forumId);
    }
    
    public String updateForumThread() {
        FacesContext context = FacesContext.getCurrentInstance();
        currentForumThread.setTitle(title);
        try {
            forumSessionBeanLocal.updateForumThread(currentForumThread, userId);
            System.out.println("Success");
        } catch (ForumThreadNotFoundException | UnauthorisedRequestException | UserNotFoundException e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
        return String.format("forum-thread.xhtml?threadId=%d&forumId=%d&faces-redirect=true", currentThreadId, forumId);
    }
    
    public String deleteThread() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            forumSessionBeanLocal.deleteForumThread(currentForumThread, userId);
            return "forum.xhtml?fId=" + forumId + "&faces-redirect=true";
        } catch (CannotDeleteException | ForumThreadNotFoundException | UnauthorisedRequestException | UserNotFoundException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
            return "";
        }
    }
    
    public void closeThread(boolean isClosed) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            forumSessionBeanLocal.closeForumThread(currentThreadId, closedById, isClosed);
        } catch (ForumThreadNotFoundException | UnauthorisedRequestException | UserNotFoundException e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }
    
    public String getCloseThreadLabel(boolean isClosed) {
        return isClosed ? "Open" : "Close";
    }

    public void openPostReply() {
        showReplyEditor = true;
        PrimeFaces.current().scrollTo("thread-form:post-editor");
    }
    
    public void createPostReply() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            Post post = new Post();
            post.setOwner(userSessionBeanLocal.getUser(userId));
            post.setDateCreated(new Date());
            post.setContent(postContent);
            forumSessionBeanLocal.createPost(post, currentForumThread);
            postContent = "";
        } catch (ForumThreadNotFoundException | UserNotFoundException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }
    
    public void updatePost() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            Post post = forumSessionBeanLocal.getPost(postToEditId);
            post.setContent(editPostContent);
            forumSessionBeanLocal.updatePost(post, userId);
            postToEditId = null;
        } catch (PostNotFoundException | UnauthorisedRequestException | UserNotFoundException e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }
    
    public void deletePost() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            forumSessionBeanLocal.deletePost(forumSessionBeanLocal.getPost(postToEditId), userId);
        } catch (PostNotFoundException | UnauthorisedRequestException | UserNotFoundException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }
    
    public void incrementViewCount() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            forumSessionBeanLocal.incrementThreadViewCount(currentThreadId);
        } catch (ForumThreadNotFoundException e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public List<ForumThread> getMyRecentForumThreads() {
        return myRecentForumThreads;
    }

    public void setMyRecentForumThreads(List<ForumThread> myRecentForumThreads) {
        this.myRecentForumThreads = myRecentForumThreads;
    }

    public Long getForumId() {
        return forumId;
    }

    public void setForumId(Long forumId) {
        this.forumId = forumId;
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public boolean isIsClosed() {
        return isClosed;
    }

    public void setIsClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    public Long getClosedById() {
        return closedById;
    }

    public void setClosedById(Long closedById) {
        this.closedById = closedById;
    }

    public String getFirstPostContent() {
        return firstPostContent;
    }

    public void setFirstPostContent(String firstPostContent) {
        this.firstPostContent = firstPostContent;
    }

    public Long getCurrentThreadId() {
        return currentThreadId;
    }

    public void setCurrentThreadId(Long currentThreadId) {
        this.currentThreadId = currentThreadId;
    }

    public ForumThread getCurrentForumThread() {
        return currentForumThread;
    }

    public void setCurrentForumThread(ForumThread currentForumThread) {
        this.currentForumThread = currentForumThread;
    }

    public boolean isShowReplyEditor() {
        return showReplyEditor;
    }

    public void setShowReplyEditor(boolean showReplyEditor) {
        this.showReplyEditor = showReplyEditor;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public boolean isShowInPostEditor() {
        return showInPostEditor;
    }

    public void setShowInPostEditor(boolean showInPostEditor) {
        this.showInPostEditor = showInPostEditor;
    }

    public Long getPostToEditId() {
        return postToEditId;
    }

    public void setPostToEditId(Long postToEditId) {
        this.postToEditId = postToEditId;
    }

    public String getEditPostContent() {
        return editPostContent;
    }

    public void setEditPostContent(String editPostContent) {
        this.editPostContent = editPostContent;
    }

}
