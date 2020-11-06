package cn.benbenedu.sundial.broadcast.controller;

import cn.benbenedu.sundial.broadcast.exception.ErrorResponseException;
import cn.benbenedu.sundial.broadcast.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class AppWideExceptionHandler {

    @ExceptionHandler(ErrorResponseException.class)
    public ResponseEntity<ErrorResponse> errorResponseHandler(
            final ErrorResponseException exception) {

        final var errorResponse = exception.getErrorResponse();

        return new ResponseEntity<>(
                errorResponse,
                errorResponse.getCode().getHttpStatus()
        );
    }
}
