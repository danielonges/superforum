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
import exception.CannotDeleteException;
import exception.ForumCategoryExistsException;
import exception.ForumCategoryNotFoundException;
import exception.ForumNotFoundException;
import exception.ForumThreadNotFoundException;
import exception.PostNotFoundException;
import exception.UnauthorisedRequestException;
import exception.UserNotFoundException;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author danielonges
 */
@Stateless
public class ForumSessionBean implements ForumSessionBeanLocal {
    
    @EJB
    private UserSessionBeanLocal userSessionBeanLocal;
    @PersistenceContext(unitName = "SuperForum-ejbPU")
    private EntityManager em;
    
    @Override
    public void createForum(Forum forum, Long adminId) throws UnauthorisedRequestException, UserNotFoundException {
        if (!isAdmin(adminId)) {
            throw new UnauthorisedRequestException("Only admin can perform this request.");
        }
        
        // check if category already exists
        Query q = em.createQuery("SELECT fc FROM ForumCategory fc WHERE LOWER(fc.category) = :inCategory");
        q.setParameter("inCategory", forum.getForumCategory().getCategory().toLowerCase());
        try {
            ForumCategory forumCategory = (ForumCategory) q.getSingleResult();
            forum.setForumCategory(forumCategory);
        } catch (Exception e) {
            em.persist(forum.getForumCategory());
        }
        em.persist(forum);
        em.flush();
    }
    
    @Override
    public List<Forum> searchForumsByTitle(String title) {
        Query q;
        if (title != null) {
            q = em.createQuery("SELECT f FROM Forum f WHERE "
                    + "LOWER(f.title) LIKE :inTitle");
            q.setParameter("inTitle", "%" + title.toLowerCase() + "%");
        } else {
            q = em.createQuery("SELECT f FROM Forum f");
        }
        return q.getResultList();
    }
    
    @Override
    public List<Forum> searchForumsByCategory(ForumCategory forumCategory) {
        Query q = em.createQuery("SELECT f FROM Forum f WHERE f.forumCategory = :inForumCategory");
        q.setParameter("inForumCategory", forumCategory);
        return q.getResultList();
    }
    
    @Override
    public Forum getForum(Long fId) throws ForumNotFoundException {
        Forum forum = em.find(Forum.class, fId);
        
        if (forum != null) {
            return forum;
        } else {
            throw new ForumNotFoundException("Forum with id " + fId + " not found!");
        }
    }
    
    @Override
    public Forum getForumByForumThread(ForumThread ft) throws ForumNotFoundException {
        Query q = em.createQuery("SELECT f FROM Forum f WHERE :inForumThread MEMBER OF f.forumThreads");
        q.setParameter("inForumThread", ft);
        try {
            return (Forum) q.getSingleResult();
        } catch (NoResultException e) {
            throw new ForumNotFoundException("No forum containing forum thread \"" + ft.getTitle() + "\" exists!");
        }
    }
   
    @Override
    public List<ForumCategory> getAllForumCategories() {
        Query q = em.createQuery("SELECT fc FROM ForumCategory fc");
        return q.getResultList();
    }
    
    @Override
    public void createForumCategory(String category) throws ForumCategoryExistsException {
        Query q = em.createQuery("SELECT fc FROM ForumCategory fc WHERE fc.category = :inCategory");
        q.setParameter("inCategory", category);
        if (q.getResultList().size() == 0) {
            em.persist(new ForumCategory(category));
        } else {
            throw new ForumCategoryExistsException("Forum category already exists!");
        }
    }
    
    @Override
    public ForumCategory getForumCategoryByName(String category) throws ForumCategoryNotFoundException {
        Query q = em.createQuery("SELECT fc FROM ForumCategory fc WHERE fc.category = :inCategory");
        q.setParameter("inCategory", category);
        try {
            return (ForumCategory) q.getSingleResult();
        } catch (NoResultException e) {
            throw new ForumCategoryNotFoundException("Forum category with name " + category + " not found!");
        }
    }
    
    @Override
    public void updateForum(Forum forum, Long adminId) throws UnauthorisedRequestException, UserNotFoundException, ForumNotFoundException {
        if (!isAdmin(adminId)) {
            throw new UnauthorisedRequestException("Only admin can perform this request.");
        }
        
        Forum forumToUpdate = getForum(forum.getId());
        ForumCategory forumCategory;
        
        // check if category already exists
        Query q = em.createQuery("SELECT fc FROM ForumCategory fc WHERE LOWER(fc.category) = :inCategory");
        q.setParameter("inCategory", forum.getForumCategory().getCategory().toLowerCase());
        try {
            forumCategory = (ForumCategory) q.getSingleResult();
            forum.setForumCategory(forumCategory);
        } catch (Exception e) {
            em.persist(forum.getForumCategory());
            forumCategory = forum.getForumCategory();
        }
        
        forumToUpdate.setTitle(forum.getTitle());
        forumToUpdate.setDescription(forum.getDescription());
        forumToUpdate.setForumCategory(forumCategory);
    }
    
    @Override
    public void deleteForum(Forum forum, Long adminId) throws UnauthorisedRequestException, UserNotFoundException, ForumNotFoundException {
        if (!isAdmin(adminId)) {
            throw new UnauthorisedRequestException("Only admin can perform this request.");
        }
        
        Forum forumToDelete = getForum(forum.getId());
        em.remove(forumToDelete);
    }
    
    @Override
    public List<ForumThread> searchThreadsByTitleInForum(Forum forum, String title) {
        Query q;
        if (title != null) {
            q = em.createQuery("SELECT t FROM ForumThread t WHERE t IN :inForumThreads AND LOWER(t.title) = :inTitle");
            q.setParameter("inForumThreads", forum.getForumThreads());
            q.setParameter("inTitle", "%" + title.toLowerCase() + "%");
        } else {
            q = em.createQuery("SELECT t FROM ForumThread t WHERE t IN :inForumThreads");
            q.setParameter("inForumThreads", forum.getForumThreads());
        }
        return q.getResultList();
    }
      
    @Override
    public List<ForumThread> getUserForumThreads(Long userId) throws UserNotFoundException {
        UserEntity userEntity = userSessionBeanLocal.getUser(userId);
        Query q = em.createQuery("SELECT t FROM ForumThread t WHERE t.owner = :inOwner");
        q.setParameter("inOwner", userEntity);
        return q.getResultList();
    }
    
    @Override
    public ForumThread getForumThread(Long ftId) throws ForumThreadNotFoundException {
        ForumThread forumThread = em.find(ForumThread.class, ftId);
        
        if (forumThread != null) {
            return forumThread;
        } else {
            throw new ForumThreadNotFoundException("Forum thread with id " + ftId + " not found!");
        }
    }
    
    @Override
    public void createForumThread(Long forumId, ForumThread forumThread) throws ForumNotFoundException {
        em.persist(forumThread);
        
        Forum forumToAdd = getForum(forumId);
        forumToAdd.getForumThreads().add(forumThread);
        em.flush();
    }
    
    @Override
    public void updateForumThread(ForumThread forumThread, Long userId) throws UnauthorisedRequestException, ForumThreadNotFoundException, UserNotFoundException {
        ForumThread forumThreadToUpdate = getForumThread(forumThread.getId());
        if (!Objects.equals(forumThreadToUpdate.getOwner().getId(), userId) && !isAdmin(userId)) {
            throw new UnauthorisedRequestException("User is not authorised for this operation.");
        }
        
        forumThreadToUpdate.setTitle(forumThread.getTitle());
    }
    
    @Override
    public void deleteForumThread(ForumThread forumThread, Long userId) throws UnauthorisedRequestException, ForumThreadNotFoundException, UserNotFoundException, CannotDeleteException {
        ForumThread forumThreadToDelete = getForumThread(forumThread.getId());
        if (forumThreadToDelete.getPosts().size() > 0) {
            throw new CannotDeleteException("This forum thread still has posts! Unable to delete.");
        }
        if (!Objects.equals(forumThreadToDelete.getOwner().getId(), userId) && !isAdmin(userId)) {
            throw new UnauthorisedRequestException("User is not authorised for this operation.");
        }
        Query q = em.createQuery("SELECT f FROM Forum f WHERE :inForumThread MEMBER OF f.forumThreads");
        q.setParameter("inForumThread", forumThreadToDelete);
        
        Forum forum = (Forum) q.getSingleResult();
        forum.getForumThreads().remove(forumThreadToDelete);
        em.remove(forumThreadToDelete);
    }
    
    @Override
    public void closeForumThread(Long threadId, Long closedById, boolean isClosed) throws ForumThreadNotFoundException, UnauthorisedRequestException, UserNotFoundException {
        ForumThread forumThread = getForumThread(threadId);
        if (!Objects.equals(forumThread.getOwner().getId(), closedById) && !isAdmin(closedById)) {
            throw new UnauthorisedRequestException("User is not authorised for this operation.");
        }
        
        forumThread.setIsClosed(isClosed);
        if (isClosed) {
            forumThread.setClosedBy(userSessionBeanLocal.getUser(closedById));
        } else {
            forumThread.setClosedBy(null);
        }
    }
    
    @Override
    public void incrementThreadViewCount(Long threadId) throws ForumThreadNotFoundException {
        ForumThread forumThread = getForumThread(threadId);
        forumThread.incrementViewCount();
    }
    
    @Override
    public Post getPost(Long pId) throws PostNotFoundException {
        Post post;
        post = em.find(Post.class, pId);
        
        if (post != null) {
            return post;
        } else {
            throw new PostNotFoundException("Post with id " + pId + " not found!");
        }
    }
    
    @Override
    public List<Post> searchPostsByContentInForumThread(ForumThread forumThread, String content) {
        Query q;
        if (content != null) {
            q = em.createQuery("SELECT p FROM Post p WHERE p IN :inPosts AND LOWER(p.content) = :inContent");
            q.setParameter("inPosts", forumThread.getPosts());
            q.setParameter("inContent", "%" + content.toLowerCase() + "%");
        } else {
            q = em.createQuery("SELECT p FROM Post p WHERE p IN :inPosts");
            q.setParameter("inPosts", forumThread.getPosts());
        }
        return q.getResultList();
    }
    
    @Override
    public void createPost(Post post, ForumThread forumThread) throws ForumThreadNotFoundException {
        em.persist(post);
        
        ForumThread forumThreadToAdd = getForumThread(forumThread.getId());
        forumThreadToAdd.getPosts().add(post);
    }
    
    @Override
    public void updatePost(Post post, Long userId) throws PostNotFoundException, UnauthorisedRequestException, UserNotFoundException {
        Post postToUpdate = getPost(post.getId());
        if (!Objects.equals(postToUpdate.getOwner().getId(), userId) && !isAdmin(userId)) {
            throw new UnauthorisedRequestException("User is not authorised for this operation.");
        }
        postToUpdate.setContent(post.getContent());
    }
    
    @Override
    public void deletePost(Post post, Long userId) throws PostNotFoundException, UnauthorisedRequestException, UserNotFoundException {
        Post postToDelete = getPost(post.getId());
        if (!Objects.equals(postToDelete.getOwner().getId(), userId) && !isAdmin(userId)) {
            throw new UnauthorisedRequestException("User is not authorised for this operation.");
        }
        Query q = em.createQuery("SELECT ft FROM ForumThread ft WHERE :inPost MEMBER OF ft.posts");
        q.setParameter("inPost", postToDelete);
        
        ForumThread forumThread = (ForumThread) q.getSingleResult();
        forumThread.getPosts().remove(postToDelete);
        em.remove(postToDelete);
    }
    
    private boolean isAdmin(Long adminId) throws UserNotFoundException {
        UserEntity admin = userSessionBeanLocal.getUser(adminId);
        return admin.getUserType() == UserType.ADMIN;
    }
    
}
