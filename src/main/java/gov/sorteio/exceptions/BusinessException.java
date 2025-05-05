package gov.sorteio.exceptions;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends ExceptionHandler{
    private static final HttpStatus status = HttpStatus.CONFLICT;

    @Builder
    public BusinessException(String code, String title, String detail,
                             String exception, Throwable trace) {
        super(code, title, status, detail, exception, trace);
    }
}
