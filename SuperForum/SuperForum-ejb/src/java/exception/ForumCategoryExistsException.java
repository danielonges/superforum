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
public class ForumCategoryExistsException extends Exception {

    /**
     * Creates a new instance of <code>ForumCategoryExistsException</code>
     * without detail message.
     */
    public ForumCategoryExistsException() {
    }

    /**
     * Constructs an instance of <code>ForumCategoryExistsException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ForumCategoryExistsException(String msg) {
        super(msg);
    }
}
