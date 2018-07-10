package nl.utwente.ing.session;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @RequestMapping(method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createSession() {
        return new ResponseEntity<>(this.sessionService.create(), HttpStatus.CREATED);
    }

    /*
     * Endpoint to retrieve balance from session for testing.
     */
    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getSession() {
        return new ResponseEntity<>(this.sessionService.getCurrent(), HttpStatus.OK);
    }

    // create persistent admin session
    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        sessionService.create("xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx");
    }
}
