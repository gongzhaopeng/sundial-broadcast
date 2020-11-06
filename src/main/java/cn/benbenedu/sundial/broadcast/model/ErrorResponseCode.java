package cn.benbenedu.sundial.broadcast.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorResponseCode {

    Undefined(HttpStatus.INTERNAL_SERVER_ERROR),
    AccessLimited(HttpStatus.UNAUTHORIZED),
    InvalidReqParams(HttpStatus.UNPROCESSABLE_ENTITY),
    ExternalApiInvokeError(HttpStatus.INTERNAL_SERVER_ERROR),
    SeverIsTooBusy(HttpStatus.SERVICE_UNAVAILABLE),
    SererConfigError(HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatus httpStatus;
}
