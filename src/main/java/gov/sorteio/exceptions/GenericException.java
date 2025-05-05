package gov.sorteio.exceptions;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GenericException extends ExceptionHandler {
    private static final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    @Builder
    public GenericException(String code, String title, String detail,
                            String exception, Throwable trace) {
        super(code, title, status, detail, exception, trace);
    }

    public GenericException(String code, String title, String detail,
                            String exception, Throwable trace, HttpStatus httpStatus) {
        super(code, title, httpStatus, detail, exception, trace);
    }
}
