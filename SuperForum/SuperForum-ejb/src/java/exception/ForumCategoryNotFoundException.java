/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author danielonges
 */
public class ForumCategoryNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>ForumCategoryNotFoundException</code>
     * without detail message.
     */
    public ForumCategoryNotFoundException() {
    }

    /**
     * Constructs an instance of <code>ForumCategoryNotFoundException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ForumCategoryNotFoundException(String msg) {
        super(msg);
    }
}
