/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.UserEntity;
import exception.UnauthorisedRequestException;
import exception.UserExistsException;
import exception.UserNotFoundException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author danielonges
 */
@Local
public interface UserSessionBeanLocal {

    public void createUser(UserEntity user) throws UserExistsException;

    public UserEntity getUser(Long uId) throws UserNotFoundException;

    public List<UserEntity> searchUsers(String username);

    public void updateUser(UserEntity user) throws UserNotFoundException;

    public void blockUser(UserEntity user, Long adminId, boolean isBlocked) throws UnauthorisedRequestException, UserNotFoundException;

    public UserEntity getUserByUsername(String username) throws UserNotFoundException;

    public UserEntity getUserByEmail(String email) throws UserNotFoundException;
    
}
