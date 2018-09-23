package com.security.project.exception;

public class GeneralServiceException extends RuntimeException{

    public GeneralServiceException() {
        super();

    }

    public GeneralServiceException(String message, Throwable cause) {
        super(message, cause);

    }

    public GeneralServiceException(String message) {
        super(message);
    }

    public GeneralServiceException(Throwable cause) {
        super(cause);
    }
}
