package nl.utwente.ing.session;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public Session create() {
        String id;
        do {
            id = UUID.randomUUID().toString();
        } while (this.sessionRepository.findById(id) != null);
        Session session = new Session();
        session.setId(id);
        return this.sessionRepository.save(session);
    }

    public Session getCurrent() {
        String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return this.sessionRepository.findById(id);
    }

    public boolean verifyById(String id) {
        return this.sessionRepository.exists(id);
    }
}
