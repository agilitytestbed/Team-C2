package nl.ing.honours;

import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@Import({AutoConfiguration.class})
@RequestMapping("/transactions")
public class TransactionController {

    @RequestMapping(method = RequestMethod.GET)
    Collection<Transaction> getTransactions(@RequestParam("offset") String offeset, @RequestParam("limit") String limit) {
        return null;
    }
}
