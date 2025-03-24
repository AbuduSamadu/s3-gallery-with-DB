package com.mascot.s3gallerywithdb.exception;

public class BadRequestException extends  RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
