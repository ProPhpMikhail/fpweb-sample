package app.finplan.dto.transaction;

import java.math.BigDecimal;

public record CategorySum(
        Long id,
        String name,
        BigDecimal sum
) {
}
