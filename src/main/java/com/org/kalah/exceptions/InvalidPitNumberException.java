package com.org.kalah.exceptions;

/**
 * @author Mithun Kini on 07/Feb/2020.
 *
 * InvalidPitNumberException - This exception is thrown when invalid pit number is passed
 * while making a move.
 * Following are the invalid pit numbers
 * Pit number less than 0 and greater than 14
 * Pit number where there are no stones
 *
 */
public class InvalidPitNumberException extends RuntimeException {
    public InvalidPitNumberException(String message) {
        super(message);
    }
}
