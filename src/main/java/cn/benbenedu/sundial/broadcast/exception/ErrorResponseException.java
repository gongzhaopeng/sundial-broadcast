package cn.benbenedu.sundial.broadcast.exception;

import cn.benbenedu.sundial.broadcast.model.ErrorResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ErrorResponseException extends RuntimeException {

    final private ErrorResponse errorResponse;
}
