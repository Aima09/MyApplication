package com.bjbsh.linford.bookapp.Exception;

/**
 * Created by 艾特不出先生 on 9/17 0017.
 */

public class BookException extends Exception {

    public BookException(){
        super();
    }
    public BookException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookException(Throwable cause) {
        super(cause);
    }

    public BookException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BookException(String message) {
        super(message);
    }
}
