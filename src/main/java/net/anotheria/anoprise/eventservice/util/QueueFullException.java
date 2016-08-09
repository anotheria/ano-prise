package net.anotheria.anoprise.eventservice.util;

import net.anotheria.anoprise.eventservice.Event;

public class QueueFullException extends Exception {

    public QueueFullException(Event target, String message, Throwable cause) {
        super("Thrown away target event (" + target + "), additional info:  " + message, cause);
    }


}
