package gov.sorteio.config;

import gov.sorteio.exceptions.ExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class RestExceptionHandler extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest atributos, ErrorAttributeOptions options) {
        final Map<String, Object> errorAtributes = super.getErrorAttributes(atributos, options);
        final Throwable error = super.getError(atributos);
        if (error instanceof final ExceptionHandler exception) {
            errorAtributes.put("codigo", exception.getCodigo());
            errorAtributes.put("title", exception.getTitle());
            errorAtributes.put("detail", exception.getDetail());
            errorAtributes.put("exception", exception.getException());
            return errorAtributes;
        }
        if (Objects.nonNull(error)) {
            errorAtributes.computeIfAbsent("exception", ex -> error.getClass().getSimpleName());

            Map<String, String> traceSMT = new HashMap<>(2);
            traceSMT.computeIfAbsent("messageError", msg -> error.getMessage());
            String cause = null;
            if (Objects.nonNull(error.getCause())) {
                cause = error.getCause().toString();
            }
            if (error instanceof MethodArgumentNotValidException) {
                Map<String, String> errors = new HashMap<>();
                ((MethodArgumentNotValidException) error).getBindingResult().getAllErrors().forEach(ex -> {
                    String fieldName = ((FieldError) ex).getField();
                    String errorMessage = ex.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });
                cause = errors.toString();
            }
            if (error instanceof DataIntegrityViolationException ex) {
                cause = ex.getRootCause().getMessage();
            }
            String finalCause = cause;
            errorAtributes.put("detail", cause);
            traceSMT.computeIfAbsent("causeError", msg -> finalCause);
            traceSMT.computeIfAbsent("exceptionError", msg -> error.getClass().getSimpleName());

            errorAtributes.putIfAbsent("trace", traceSMT);
            return errorAtributes;
        }
        errorAtributes.putIfAbsent("codigo", "COD-000");
        errorAtributes.putIfAbsent("title", "Erro ao realizar operação");
        errorAtributes.putIfAbsent("detail", "Desculpe, tivemos um problema. Tente novamente em instantes.");
        return errorAtributes;
    }
}