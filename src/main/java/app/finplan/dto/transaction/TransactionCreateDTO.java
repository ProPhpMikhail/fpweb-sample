package app.finplan.dto.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionCreateDTO {
    @NotBlank
    String name;
    @NotNull
    BigDecimal amount;
    Long categoryId = null;
    LocalDateTime createdAt;
    BigDecimal latitude = null;
    BigDecimal longitude = null;
    @Override
    public String toString() {
        return "TransactionCreateDTO{" +
                "name='" + name + '\'' +
                ", amount=" + amount +
                '}';
    }
}
