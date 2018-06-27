package nl.utwente.ing.transaction;

import nl.utwente.ing.category.Category;
import nl.utwente.ing.session.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySession(Session session);

    Transaction findBySessionAndId(Session session, Long id);

    List<Transaction> findByCategoryAndSession(Category category, Session session);

    @Query("select t from Transaction t where t.session = ?1 " +
            "and lower(t.description) like concat('%', lower(?2), '%') " +
            "and lower(t.externalIBAN) like concat('%', lower(?3), '%') " +
            "and (t.type = ?4 or ?4 is null) order by t.id asc")
    List<Transaction> findTransactionsByCategoryRule(Session session, String description, String iban, Transaction.Type type);
}
