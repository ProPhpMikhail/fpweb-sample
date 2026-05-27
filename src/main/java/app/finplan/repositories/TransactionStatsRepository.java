package app.finplan.repositories;

import app.finplan.dto.transaction.CategorySum;
import app.finplan.model.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TransactionStatsRepository {

    @PersistenceContext
    private final EntityManager em;

    public List<CategorySum> sumByCategory(Specification<Transaction> spec, JoinType joinType) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CategorySum> cq = cb.createQuery(CategorySum.class);

        Root<Transaction> t = cq.from(Transaction.class);
        Join<Transaction, Category> c = t.join("category", joinType);
        Expression<BigDecimal> total = cb.sum(t.get("amount"));

        cq.select(cb.construct(CategorySum.class, c.get("id"), c.get("name"), total));
        cq.groupBy(c.get("id"));

        if (spec != null) {
            Predicate p = spec.toPredicate(t, cq, cb);
            if (p != null) cq.where(p);
        }

        return em.createQuery(cq).getResultList();
    }

    public List<CategorySum> sumByAccount(Specification<Transaction> spec) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<CategorySum> cq = cb.createQuery(CategorySum.class);

        Root<Transaction> t = cq.from(Transaction.class);
        Join<Transaction, Account> acc = t.join("account", JoinType.INNER);
        Expression<BigDecimal> total = cb.sum(t.get("amount"));

        cq.select(cb.construct(CategorySum.class, acc.get("id"), acc.get("name"), total));
        cq.groupBy(acc.get("id"));

        if (spec != null) {
            Predicate p = spec.toPredicate(t, cq, cb);
            if (p != null) cq.where(p);
        }

        return em.createQuery(cq).getResultList();
    }
}
