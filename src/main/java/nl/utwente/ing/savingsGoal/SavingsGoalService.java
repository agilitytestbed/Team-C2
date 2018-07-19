package nl.utwente.ing.savingsGoal;

import nl.utwente.ing.session.Session;
import nl.utwente.ing.session.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SavingsGoalService {

    private final SavingsGoalRepository savingsGoalRepository;

    private final SessionRepository sessionRepository;

    @Autowired
    public SavingsGoalService(SavingsGoalRepository savingsGoalRepository, SessionRepository sessionRepository) {
        this.savingsGoalRepository = savingsGoalRepository;
        this.sessionRepository = sessionRepository;
    }

    public List<SavingsGoal> findBySession(Session session) {
        return savingsGoalRepository.findBySession(session);
    }

    public SavingsGoal findBySessionAndId(Session session, Long id) {
        return savingsGoalRepository.findBySessionAndId(session, id);
    }

    public SavingsGoal create(SavingsGoal savingsGoal) {
        return savingsGoalRepository.save(savingsGoal);
    }

    public void delete(SavingsGoal savingsGoal) {
        // TODO: register balance change in latest transaction (for balance history implementation).
        Session session = savingsGoal.getSession();
        session.addToBalance(savingsGoal.getBalance());
        sessionRepository.save(session);
        savingsGoalRepository.delete(savingsGoal);
    }

}
