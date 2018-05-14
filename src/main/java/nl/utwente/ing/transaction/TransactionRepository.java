package nl.utwente.ing.transaction;

import nl.utwente.ing.category.Category;
import nl.utwente.ing.session.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySession(Session session);

    Transaction findBySessionAndId(Session session, Long id);

    List<Transaction> findByCategoryAndSession(Category category, Session session);
}
