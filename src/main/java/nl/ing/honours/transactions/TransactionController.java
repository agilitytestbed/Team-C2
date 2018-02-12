package nl.ing.honours.transactions;

import nl.ing.honours.AutoConfiguration;
import nl.ing.honours.categories.CategoryRepository;
import nl.ing.honours.sessions.Session;
import nl.ing.honours.sessions.SessionRepository;
import nl.ing.honours.categories.Category;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.springframework.util.MimeTypeUtils.*;

@RestController
@Import({AutoConfiguration.class})
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Transaction> getTransactions(@RequestParam(value = "offset", required = false) String offeset,
                                                   @RequestParam(value = "limit", required = false) String limit) {
        return transactionRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = TEXT_PLAIN_VALUE)
    public ResponseEntity createTransaction(@RequestHeader(name = "WWW_Authenticate", required = false) String sessionId, @RequestBody(required = false) Transaction transaction) {
        Long id = transaction.getId();
        Date date = transaction.getDate();
        Double amount = transaction.getAmount();
        String iban = transaction.getIban();
        String type = transaction.getType();
        List<Category> categories = transaction.getCategory();

        sessionRepository.save(new Session(sessionId));
        Session session = sessionRepository.findFirstBySession(sessionId);
        Transaction existingTransaction = transactionRepository.findByIdAndSession(id, session);
        if (session == null) {
            return new ResponseEntity<>("Session ID is missing or invalid", HttpStatus.UNAUTHORIZED);
        } else if (id == null || existingTransaction != null) {
            System.out.println(existingTransaction);
            return new ResponseEntity<>("Invalid input given", HttpStatus.METHOD_NOT_ALLOWED);
        }
        transaction.setSession(session);
        if (categories != null) {
            categories.removeIf(c -> c.getId() == null);
            categories.removeIf(c -> c.getName() == null);
            for (Category c : categories) {
                if (c.getId() != null && !categoryRepository.exists(c.getId())) {
                    return new ResponseEntity<>("Invalid input given", HttpStatus.METHOD_NOT_ALLOWED);
                }
            }
        }
        transaction.setCategory(categories);
        System.out.println(id + " " + date + " " + amount + " " + iban + " " + type + " " + categories);

        transactionRepository.save(transaction);
        return new ResponseEntity<>("Successful operation", HttpStatus.CREATED);
    }
}
