package nl.ing.honours.sessions;

import nl.ing.honours.AutoConfiguration;
import nl.ing.honours.categories.Category;
import nl.ing.honours.transactions.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Random;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.util.MimeTypeUtils.TEXT_PLAIN_VALUE;

@RestController
@Import(AutoConfiguration.class)
@RequestMapping("/sessions")
public class SessionController {
    @Autowired
    private SessionRepository sessionRepository;

    @RequestMapping(method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity generateSessionsId() {
        Session session = new Session();
        Random random = new Random();
        Long sessionId;
        do {
            sessionId = random.nextLong();
        } while (sessionRepository.findBySessionId(sessionId) != null);
        session.setSessionId(sessionId);
        session.setTransactions(new ArrayList<Transaction>());
        session.setCategories(new ArrayList<Category>());
        sessionRepository.saveAndFlush(session);
        return new ResponseEntity<>(session, HttpStatus.OK);
    }
}
