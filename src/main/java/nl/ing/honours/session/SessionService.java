package nl.ing.honours.session;

import nl.ing.honours.category.Category;
import nl.ing.honours.transaction.Transaction;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session createSession() {
        String id;
        do {
            id = UUID.randomUUID().toString();
        } while (sessionRepository.findById(id) != null);
        Session session = new Session();
        session.setId(id);
        session.setTransactions(new ArrayList<Transaction>());
        session.setCategories(new ArrayList<Category>());
        return sessionRepository.save(session);
    }

    public Session getCurrentSession() {
        return (Session) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


    public Session findSessionById(String id) {
        return sessionRepository.findById(id);
    }
}
