package nl.utwente.ing.balance;

import nl.utwente.ing.session.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    private final SessionService sessionService;

    private final BalanceService balanceService;

    public BalanceController(SessionService sessionService, BalanceService balanceService) {
        this.sessionService = sessionService;
        this.balanceService = balanceService;
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getBalanceHistory(
            @RequestParam(value = "interval", required = false, defaultValue = "month") String interval,
            @RequestParam(value = "intervals", required = false, defaultValue = "24") Integer intervals) {
        List<Candlestick> history = balanceService.generateHistory(sessionService.getCurrent(), interval, intervals);
        return new ResponseEntity<>(history, HttpStatus.OK);
    }

}
