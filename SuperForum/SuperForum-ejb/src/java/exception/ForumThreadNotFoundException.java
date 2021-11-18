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
public class ForumThreadNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>ForumThreadNotFoundException</code>
     * without detail message.
     */
    public ForumThreadNotFoundException() {
    }

    /**
     * Constructs an instance of <code>ForumThreadNotFoundException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ForumThreadNotFoundException(String msg) {
        super(msg);
    }
}
