/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumeration;

/**
 *
 * @author danielonges
 */
public enum UserType {
    NORMAL("Normal"),
    ADMIN("Admin");
    
    private String userType;
    
    UserType(String userType) {
        this.userType = userType;
    }
    
    public String getUserType() {
        return userType;
    }
}
