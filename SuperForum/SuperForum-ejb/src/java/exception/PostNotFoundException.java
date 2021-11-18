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
public class PostNotFoundException extends Exception {

    /**
     * Creates a new instance of <code>PostNotFoundException</code> without
     * detail message.
     */
    public PostNotFoundException() {
    }

    /**
     * Constructs an instance of <code>PostNotFoundException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PostNotFoundException(String msg) {
        super(msg);
    }
}
