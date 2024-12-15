package cat.tecnocampus.notes2324.api.exceptionHandling;

import cat.tecnocampus.notes2324.application.exceptions.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlingAdvice {
    @ExceptionHandler(DomainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    String domainExceptionHandler(Exception ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
        List<Violation> violations = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    String message = error.getDefaultMessage();
                    String field = ((FieldError) error).getField();
                    return new Violation(field, message);
                })
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    public static class Violation {
        private final String field;
        private final String message;

        public Violation(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class ValidationErrorResponse {
        private final List<Violation> violations;

        public ValidationErrorResponse(List<Violation> violations) {
            this.violations = violations;
        }

        public List<Violation> getViolations() {
            return violations;
        }
    }
}

