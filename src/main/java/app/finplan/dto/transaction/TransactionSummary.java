package app.finplan.dto.transaction;

import java.math.BigDecimal;

public record TransactionSummary(
        String type,
        BigDecimal sum
) {}
