/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.UserEntity;
import enumeration.UserType;
import exception.UnauthorisedRequestException;
import exception.UserExistsException;
import exception.UserNotFoundException;
import java.util.List;
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
public class UserSessionBean implements UserSessionBeanLocal {

    @PersistenceContext(unitName = "SuperForum-ejbPU")
    private EntityManager em;

    @Override
    public void createUser(UserEntity user) throws UserExistsException {
        UserEntity userWithUsername = null;
        UserEntity userWithEmail = null;
        try {
            userWithUsername = getUserByUsername(user.getUsername());
        } catch (UserNotFoundException ex) {
        }
        try {
            userWithEmail = getUserByEmail(user.getEmail());
        } catch (UserNotFoundException e) {
        }
        
        if (userWithUsername != null) {
            throw new UserExistsException("User with username " + user.getUsername() + " already exists!");
        } else if (userWithEmail != null) {
            throw new UserExistsException("User with email " + user.getEmail() + " already exists!");
        } else {
            em.persist(user);
        }
    }

    @Override
    public UserEntity getUser(Long uId) throws UserNotFoundException {
        UserEntity user = em.find(UserEntity.class, uId);

        if (user != null) {
            return user;
        } else {
            throw new UserNotFoundException("User with id " + uId + " not found!");
        }
    }

    @Override
    public UserEntity getUserByUsername(String username) throws UserNotFoundException {
        Query q = em.createQuery("SELECT u FROM UserEntity u WHERE u.username = :inUsername");
        q.setParameter("inUsername", username);

        try {
            UserEntity user = (UserEntity) q.getSingleResult();
            return user;
        } catch (NoResultException e) {
            throw new UserNotFoundException("User with username " + username + " not found!");
        }
    }

    @Override
    public UserEntity getUserByEmail(String email) throws UserNotFoundException {
        Query q = em.createQuery("SELECT u FROM UserEntity u WHERE u.email = :inEmail");
        q.setParameter("inEmail", email);

        try {
            UserEntity user = (UserEntity) q.getSingleResult();
            return user;
        } catch (NoResultException e) {
            throw new UserNotFoundException("User with email " + email + " not found!");
        }
    }

    @Override
    public List<UserEntity> searchUsers(String username) {
        Query q;
        if (username != null) {
            q = em.createQuery("SELECT u FROM UserEntity u WHERE LOWER(u.username) LIKE :inUsername");
            q.setParameter("inUsername", "%" + username.toLowerCase() + "%");
        } else {
            q = em.createQuery("SELECT u FROM UserEntity u");
        }
        return q.getResultList();
    }

    @Override
    public void updateUser(UserEntity user) throws UserNotFoundException {
        UserEntity userToUpdate = getUser(user.getId());
        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setProfilePhoto(user.getProfilePhoto());
        userToUpdate.setBio(user.getBio());
        em.flush();
    }

    @Override
    public void blockUser(UserEntity user, Long adminId, boolean isBlocked) throws UnauthorisedRequestException, UserNotFoundException {
        UserEntity admin = getUser(adminId);
        if (admin.getUserType() != UserType.ADMIN) {
            throw new UnauthorisedRequestException("Only admin can perform this request.");
        }

        UserEntity userToBlock = getUser(user.getId());
        userToBlock.setIsBlocked(isBlocked);
    }
}
