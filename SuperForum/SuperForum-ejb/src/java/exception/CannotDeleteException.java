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
public class CannotDeleteException extends Exception {

    /**
     * Creates a new instance of <code>CannotDeleteException</code> without
     * detail message.
     */
    public CannotDeleteException() {
    }

    /**
     * Constructs an instance of <code>CannotDeleteException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CannotDeleteException(String msg) {
        super(msg);
    }
}
