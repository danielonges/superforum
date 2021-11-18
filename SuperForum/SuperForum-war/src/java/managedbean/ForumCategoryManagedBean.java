/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean;

import entity.Forum;
import entity.ForumCategory;
import exception.ForumCategoryNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import session.ForumSessionBeanLocal;

/**
 *
 * @author danielonges
 */
@Named(value = "forumCategoryManagedBean")
@ViewScoped
public class ForumCategoryManagedBean implements Serializable {
    
    @EJB
    private ForumSessionBeanLocal forumSessionBeanLocal;
    
    private String category;
    private List<Forum> forums;

    /**
     * Creates a new instance of ForumCategoryManagedBean
     */
    public ForumCategoryManagedBean() {
    }
    
    public void loadCategoryForums() {
        try {
            ForumCategory forumCategory = forumSessionBeanLocal.getForumCategoryByName(category);
            forums = forumSessionBeanLocal.searchForumsByCategory(forumCategory);
        } catch (ForumCategoryNotFoundException e) {
            forums = new ArrayList<>();
        }
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Forum> getForums() {
        return forums;
    }

    public void setForums(List<Forum> forums) {
        this.forums = forums;
    }
    
    
}
