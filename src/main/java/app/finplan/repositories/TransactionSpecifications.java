package app.finplan.repositories;

import app.finplan.model.Transaction;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionSpecifications {

    public static Specification<Transaction> userIdEquals(Long userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Transaction> accountIdEquals(Long accountId) {
        return (root, query, cb) -> {
            if (accountId == null) return null;
            return cb.equal(root.get("account").get("id"), accountId);
        };
    }

    public static Specification<Transaction> currencyEquals(Currency currency) {
        return (root, query, cb) -> {
            if (currency == null) return null;
            return cb.equal(root.get("account").get("currency"), currency);
        };
    }

    public static Specification<Transaction> accountIdIn(List<Long> accountIds) {
        return (root, query, cb) -> {
            if (accountIds == null || accountIds.isEmpty()) return null;

            return root.get("account").get("id").in(accountIds);
        };
    }

    public static Specification<Transaction> type(String type) {
        return (root, query, cb) -> {
            if (type == null || type.isBlank()) return null;
            return type.equals("expense")
                    ? cb.lessThan(root.get("amount"), 0)
                    : cb.greaterThanOrEqualTo(root.get("amount"), 0);
        };
    }

    public static Specification<Transaction> categoryIdEquals(Long categoryId) {
        return (root, query, cb) -> {
            if (categoryId == null) return null;
            return cb.equal(root.get("category").get("id"), categoryId);
        };
    }

    public static Specification<Transaction> nameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) return null;
            String pattern = "%" + name.trim().toLowerCase() + "%";
            return cb.like(cb.lower(root.get("name")), pattern);
        };
    }

    public static Specification<Transaction> amountBetween(BigDecimal from, BigDecimal to) {
        return (root, query, cb) -> {
            if (from == null && to == null) return null;

            if (from != null && to != null) {
                return cb.between(root.get("amount"), from, to);
            } else if (from != null) {
                return cb.greaterThanOrEqualTo(root.get("amount"), from);
            } else {
                return cb.lessThanOrEqualTo(root.get("amount"), to);
            }
        };
    }

    public static Specification<Transaction> dateBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> {
            if (from == null && to == null) return null;

            if (from != null && to != null) {
                return cb.between(root.get("createdAt"), from, to);
            } else if (from != null) {
                return cb.greaterThanOrEqualTo(root.get("createdAt"), from);
            } else {
                return cb.lessThan(root.get("createdAt"), to);
            }
        };
    }
}
