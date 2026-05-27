package app.finplan.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {

    private final String code;

    private HttpStatus status = HttpStatus.BAD_REQUEST;

    public BusinessException(BusinessExCode code) {
        super(code.getMessage());
        this.code = code.getCode();
        this.status = code.getStatus();
    }

    public BusinessException(BusinessExCode code, String message) {
        super(message);
        this.code = code.getCode();
        this.status = code.getStatus();
    }
}

