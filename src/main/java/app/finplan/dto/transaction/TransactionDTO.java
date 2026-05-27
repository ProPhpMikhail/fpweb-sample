package app.finplan.dto.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionDTO {
    private Long id;
    @NotNull
    Long userId;
    @NotBlank
    String name;
    String accountName = "";
    @NotNull
    Long accountId;
    Long categoryId;
    String categoryName;
    @NotBlank
    BigDecimal amount;

    LocalDateTime createdAt;
    BigDecimal latitude;
    BigDecimal longitude;
}
