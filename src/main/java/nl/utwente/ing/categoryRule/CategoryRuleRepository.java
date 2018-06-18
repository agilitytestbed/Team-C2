package nl.utwente.ing.categoryRule;

import nl.utwente.ing.session.Session;
import nl.utwente.ing.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRuleRepository extends JpaRepository<CategoryRule, Long> {

    List<CategoryRule> findBySession(Session session);

    CategoryRule findBySessionAndId(Session session, Long id);

    @Query("select r from CategoryRule r where r.session = ?1 " +
           "and lower(?2) like concat('%', lower(r.description), '%') " +
           "and lower(?3) like concat('%', lower(r.iBAN), '%') " +
           "and (?4 = r.type or r.type is null) order by r.id asc")
    List<CategoryRule> findApplicableRules(Session session, String description, String iban, Transaction.Type type);

}
