package app.finplan.mapper;

import app.finplan.dto.transaction.TransactionCreateDTO;
import app.finplan.dto.transaction.TransactionDTO;
import app.finplan.dto.transaction.TransactionUpdateDTO;
import app.finplan.model.Transaction;
import org.mapstruct.*;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TransactionMapper {
    public abstract Transaction map(TransactionCreateDTO dto);

    @Mapping(target = "accountName", source = "model.account.name")
    @Mapping(target = "accountId", source = "model.account.id")
    @Mapping(target = "categoryName", source = "model.category.name")
    @Mapping(target = "categoryId", source = "model.category.id")
    @Mapping(target = "userId", source = "model.user.id")
    public abstract TransactionDTO map(Transaction model);
    public abstract void update(TransactionUpdateDTO dto, @MappingTarget Transaction model);
    public abstract void create(TransactionCreateDTO dto, @MappingTarget Transaction model);
}
