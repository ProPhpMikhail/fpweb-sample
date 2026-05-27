package app.finplan.repositories;

import app.finplan.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    List<Transaction> findByAccountId(Long accountId);
    List<Transaction> findByCategoryId(Long categoryId);
    Page<Transaction> findByUserId(Long userId, Pageable pageable);

    Optional<Transaction> findByIdAndUserId(Long id, Long userId);

    List<Transaction> findByAccountIdAndCreatedAtAfterOrderByCreatedAtAsc(
            Long accountId,
            LocalDateTime createdAt
    );

    @Query("""
      SELECT t FROM Transaction t
      WHERE t.account.id = :accountId
        AND (t.createdAt > :createdAt OR (t.createdAt = :createdAt AND t.id > :id))
      ORDER BY t.createdAt ASC, t.id ASC
    """)
    List<Transaction> findAfter(Long accountId, LocalDateTime createdAt, Long id);

    @Query("""
        SELECT SUM(t.amount)
        FROM Transaction t
        WHERE t.user.id = :userId
          AND (:isExpense = true AND t.amount < 0 OR :isExpense = false AND t.amount >= 0)
    """)
    BigDecimal sumByType(Long userId, boolean isExpense);
}