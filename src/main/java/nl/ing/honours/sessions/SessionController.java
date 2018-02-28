package nl.ing.honours.sessions;

import nl.ing.honours.AutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;
import static org.springframework.util.MimeTypeUtils.TEXT_PLAIN_VALUE;

@RestController
@Import(AutoConfiguration.class)
@RequestMapping("/sessions")
public class SessionController {
    @Autowired
    private SessionRepository sessionRepository;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = TEXT_PLAIN_VALUE)
    public ResponseEntity generateSessionsId() {
        String id = UUID.randomUUID().toString();
        while (sessionRepository.findFirstById(id) != null) {
            id = UUID.randomUUID().toString();
        }
        Session session = new Session(id);
        sessionRepository.save(session);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
