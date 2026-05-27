package app.finplan.exception;

import org.springframework.http.HttpStatus;

public interface BusinessExCode {
    String getCode();
    String getMessage();
    HttpStatus getStatus();
}
