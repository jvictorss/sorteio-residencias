package gov.sorteio.exceptions;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

@Getter
public class InvalidRequestException extends ExceptionHandler{
    private static final HttpStatus status = HttpStatus.BAD_REQUEST;

    @Builder
    public InvalidRequestException(String code, String title, String detail,
                                   String exception, @Nullable Throwable trace) {
        super(code, title, status, detail, exception, trace);
    }
}
