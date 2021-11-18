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
public class UserExistsException extends Exception {

    /**
     * Creates a new instance of <code>UserExistsException</code> without detail
     * message.
     */
    public UserExistsException() {
    }

    /**
     * Constructs an instance of <code>UserExistsException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public UserExistsException(String msg) {
        super(msg);
    }
}
