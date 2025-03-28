package com.mascot.s3gallerywithdb.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private HttpStatus statusCode;
    private String message;

}
