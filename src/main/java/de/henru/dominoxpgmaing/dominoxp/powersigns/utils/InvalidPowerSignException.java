/*
 * Copyright (c) 2020 Jan (dominoxp@henru.de).
 * All rights reserved.
 */

package de.henru.dominoxpgmaing.dominoxp.powersigns.utils;

/**
 * Exception being thrown if the given block is not a power sign
 */
public class InvalidPowerSignException extends Exception {
    public InvalidPowerSignException(String message) {
        super(message);
    }
}
