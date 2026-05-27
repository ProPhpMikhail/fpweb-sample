package app.finplan.dto.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionFilter (
        Long accountId,
        Long categoryId,
        String name,
        String type,
        BigDecimal amountFrom,
        BigDecimal amountTo,
        LocalDateTime createdAtFrom,
        LocalDateTime createdAtTo
) {
}
