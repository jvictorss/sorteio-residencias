package gov.sorteio.exceptions;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ResponseStatusException;

@Getter
@ToString
public class ExceptionHandler extends ResponseStatusException {
    final String codigo;
    final String title;
    final String detail;
    final String exception;
    final Throwable trace;
    public ExceptionHandler(String codigo, String title,
                            HttpStatus status, String detail,
                            String exception, @Nullable Throwable trace) {
        super(status, detail);
        this.codigo = codigo;
        this.title = title;
        this.detail = detail;
        this.exception = exception;
        this.trace = trace;
    }
}
