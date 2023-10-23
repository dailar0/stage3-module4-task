package com.mjc.school.service.exception;

public class EntityNotFoundException extends BaseException {

    public EntityNotFoundException() {
        super(ErrorCode.NOT_FOUND);
    }

    public EntityNotFoundException(String message) {
        super(ErrorCode.NOT_FOUND, message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(ErrorCode.NOT_FOUND, message, cause);
    }

    public EntityNotFoundException(Throwable cause) {
        super(ErrorCode.NOT_FOUND, cause);
    }
}
