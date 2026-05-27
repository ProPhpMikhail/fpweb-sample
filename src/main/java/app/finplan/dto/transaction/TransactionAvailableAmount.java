package app.finplan.dto.transaction;

import java.math.BigDecimal;

public record TransactionAvailableAmount(
        Long userId,
        Long accountId,
        BigDecimal amount
) {}
