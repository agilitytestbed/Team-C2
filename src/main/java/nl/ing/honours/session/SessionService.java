package nl.ing.honours.session;

import nl.ing.honours.category.Category;
import nl.ing.honours.transaction.Transaction;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Random;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final Random random;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
        this.random = new Random();
    }

    public Session createSession() {
        Long id;
        do {
            id = random.nextLong();
        } while (sessionRepository.findById(id) != null);
        Session session = new Session();
        session.setId(id);
        session.setTransactions(new ArrayList<Transaction>());
        session.setCategories(new ArrayList<Category>());
        return sessionRepository.save(session);
    }

    public Session findSessionById(Long id) {
        return sessionRepository.findById(id);
    }

    public boolean verifySessionById(Long id) {
        return sessionRepository.exists(id);
    }
}
