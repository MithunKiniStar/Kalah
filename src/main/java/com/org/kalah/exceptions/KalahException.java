package com.org.kalah.exceptions;

/**
 * @author Mithun Kini on 07/Feb/2020.
 *
 * KalahException - This exception is thrown when exception is thrown during create game
 * and make move in agame.
 */
public class KalahException extends RuntimeException {
    public KalahException(String message) {
        super(message);
    }
}
