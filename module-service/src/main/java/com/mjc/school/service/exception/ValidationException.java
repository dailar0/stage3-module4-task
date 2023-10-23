package com.mjc.school.service.exception;

public class ValidationException extends BaseException {
    public ValidationException() {
        super(ErrorCode.BAD_REQUEST);
    }

    public ValidationException(String message) {
        super(ErrorCode.BAD_REQUEST, message);
    }

    public ValidationException(String message, Throwable cause) {
        super(ErrorCode.BAD_REQUEST, message, cause);
    }

    public ValidationException(Throwable cause) {
        super(ErrorCode.BAD_REQUEST, cause);
    }
}
