package com.mjc.school.service.exception;

public class InternalServerErrorException extends BaseException {
    public InternalServerErrorException() {
        super(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    public InternalServerErrorException(String message) {
        super(ErrorCode.INTERNAL_SERVER_ERROR, message);
    }

    public InternalServerErrorException(String message, Throwable cause) {
        super(ErrorCode.INTERNAL_SERVER_ERROR, message, cause);
    }

    public InternalServerErrorException(ErrorCode errorCode, Throwable cause) {
        super(ErrorCode.INTERNAL_SERVER_ERROR, cause);
    }

}
