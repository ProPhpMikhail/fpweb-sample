package app.finplan.dto.transaction;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionUpdateDTO {
    @NotBlank
    String name;
    @NotNull(message="amount can not be null")
    @Digits(integer=12, fraction=2)
    BigDecimal amount;
    Long categoryId = null;
    LocalDateTime createdAt;
    BigDecimal latitude;
    BigDecimal longitude;

    @Override
    public String toString() {
        return "TransactionCreateDTO{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }
}
