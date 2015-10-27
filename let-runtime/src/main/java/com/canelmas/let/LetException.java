package com.canelmas.let;

/**
 * Created by can on 27/09/15.
 */
final class LetException extends RuntimeException {

    public LetException(String detailMessage) {
        super(detailMessage);
    }

    public LetException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
