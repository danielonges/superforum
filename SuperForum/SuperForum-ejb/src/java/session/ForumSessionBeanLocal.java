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
import exception.CannotDeleteException;
import exception.ForumCategoryExistsException;
import exception.ForumCategoryNotFoundException;
import exception.ForumNotFoundException;
import exception.ForumThreadNotFoundException;
import exception.PostNotFoundException;
import exception.UnauthorisedRequestException;
import exception.UserNotFoundException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author danielonges
 */
@Local
public interface ForumSessionBeanLocal {

    public void createForum(Forum forum, Long adminId) throws UnauthorisedRequestException, UserNotFoundException;

    public List<Forum> searchForumsByTitle(String title);

    public Forum getForum(Long fId) throws ForumNotFoundException;

    public void updateForum(Forum forum, Long adminId) throws UnauthorisedRequestException, UserNotFoundException, ForumNotFoundException;

    public void deleteForum(Forum forum, Long adminId) throws UnauthorisedRequestException, UserNotFoundException, ForumNotFoundException;

    public List<ForumThread> searchThreadsByTitleInForum(Forum forum, String title);

    public ForumThread getForumThread(Long ftId) throws ForumThreadNotFoundException;

    public void updateForumThread(ForumThread forumThread, Long userId) throws UnauthorisedRequestException, ForumThreadNotFoundException, UserNotFoundException;

    public void deleteForumThread(ForumThread forumThread, Long userId) throws UnauthorisedRequestException, ForumThreadNotFoundException, UserNotFoundException, CannotDeleteException;

    public Post getPost(Long pId) throws PostNotFoundException;

    public List<Post> searchPostsByContentInForumThread(ForumThread forumThread, String content);

    public void createPost(Post post, ForumThread forumThread) throws ForumThreadNotFoundException;

    public void updatePost(Post post, Long userId) throws PostNotFoundException, UnauthorisedRequestException, UserNotFoundException;

    public void deletePost(Post post, Long userId) throws PostNotFoundException, UnauthorisedRequestException, UserNotFoundException;

    public List<ForumCategory> getAllForumCategories();

    public void createForumThread(Long forumId, ForumThread forumThread) throws ForumNotFoundException;

    public List<ForumThread> getUserForumThreads(Long userId) throws UserNotFoundException;

    public List<Forum> searchForumsByCategory(ForumCategory forumCategory);

    public ForumCategory getForumCategoryByName(String category) throws ForumCategoryNotFoundException;

    public Forum getForumByForumThread(ForumThread ft) throws ForumNotFoundException;

    public void createForumCategory(String category) throws ForumCategoryExistsException;

    public void incrementThreadViewCount(Long threadId) throws ForumThreadNotFoundException;

    public void closeForumThread(Long threadId, Long closedById, boolean isClosed) throws ForumThreadNotFoundException, UnauthorisedRequestException, UserNotFoundException;
    
}
