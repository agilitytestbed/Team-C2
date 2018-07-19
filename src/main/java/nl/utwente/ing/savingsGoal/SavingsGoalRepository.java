package nl.utwente.ing.savingsGoal;

import nl.utwente.ing.session.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Long> {

    List<SavingsGoal> findBySession(Session session);

    SavingsGoal findBySessionAndId(Session session, Long id);

}
