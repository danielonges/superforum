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
public class UnauthorisedRequestException extends Exception {

    /**
     * Creates a new instance of <code>UnauthorisedRequestException</code>
     * without detail message.
     */
    public UnauthorisedRequestException() {
    }

    /**
     * Constructs an instance of <code>UnauthorisedRequestException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public UnauthorisedRequestException(String msg) {
        super(msg);
    }
}
