package com.bjbsh.linford.bookapp.Exception;

/**
 * Created by 艾特不出先生 on 9/17 0017.
 */

public class ClassifyException extends Exception {
    public ClassifyException() {
    }

    public ClassifyException(String message) {
        super(message);
    }

    public ClassifyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClassifyException(Throwable cause) {
        super(cause);
    }

    public ClassifyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
