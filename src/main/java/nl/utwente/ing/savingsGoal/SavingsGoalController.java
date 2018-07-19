package nl.utwente.ing.savingsGoal;

import nl.utwente.ing.exceptions.ResourceNotFoundException;
import nl.utwente.ing.session.Session;
import nl.utwente.ing.session.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/savingGoals")
public class SavingsGoalController {

    private final SavingsGoalService savingsGoalService;

    private final SessionService sessionService;

    public SavingsGoalController(SavingsGoalService savingsGoalService, SessionService sessionService) {
        this.savingsGoalService = savingsGoalService;
        this.sessionService = sessionService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getSavingGoals() {
        List<SavingsGoal> savingsGoals = savingsGoalService.findBySession(sessionService.getCurrent());
        return new ResponseEntity<>(savingsGoals, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity createSavingsGoal(@RequestBody SavingsGoal data) {
        data.setSession(sessionService.getCurrent());
        SavingsGoal savingsGoal = savingsGoalService.create(data);
        return new ResponseEntity<>(savingsGoal, HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteSavingsGoal(@PathVariable(name = "id") Long id) {
        Session session = sessionService.getCurrent();
        SavingsGoal savingsGoal = savingsGoalService.findBySessionAndId(session, id);
        if (savingsGoal == null) {
            throw new ResourceNotFoundException();
        }
        savingsGoalService.delete(savingsGoal);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
