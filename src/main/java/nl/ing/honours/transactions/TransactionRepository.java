package nl.ing.honours.transactions;

import nl.ing.honours.sessions.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByIdAndSession(Long id, Session session);
}
