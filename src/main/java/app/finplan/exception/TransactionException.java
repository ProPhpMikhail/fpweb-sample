package app.finplan.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum TransactionException implements BusinessExCode {
    NOT_FOUND(
            "transaction_not_found",
            "Transaction not found",
            HttpStatus.NOT_FOUND
    );

    private final String code;
    private final String message;
    private final HttpStatus status;
}
