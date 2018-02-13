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

import java.util.Comparator;
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
    public ResponseEntity getTransactions(@RequestHeader(name = "WWW_Authenticate", required = false) String sessionId,
                                      @RequestParam(value = "offset", required = false) Integer offset,
                                      @RequestParam(value = "limit", required = false) Integer limit) {
        Session session = sessionRepository.findFirstById(sessionId);
        if (session == new Session()) {
            return new ResponseEntity<>("Session ID is missing or invalid", HttpStatus.UNAUTHORIZED);
        } else if (offset == null) {
            offset = 0;
        } else if (limit == null) {
            limit = 0;
        }
        List<Transaction> transactions = session.getTransactions();
        if (limit > transactions.size()) {
            limit = transactions.size();
        } else if (offset > transactions.size()) {
            offset = transactions.size();
        }
        transactions.sort(Comparator.comparing(Transaction::getId));
        transactions.subList(offset, limit);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = TEXT_PLAIN_VALUE)
    public ResponseEntity createTransaction(@RequestHeader(name = "WWW_Authenticate", required = false) String sessionId, @RequestBody(required = false) Transaction transaction) {
        Long id = transaction.getId();
        Date date = transaction.getDate();
        Double amount = transaction.getAmount();
        String iban = transaction.getIban();
        String type = transaction.getType();
        List<Category> categories = transaction.getCategory();


        Session session = sessionRepository.findFirstById(sessionId);
        Transaction existingTransaction = transactionRepository.findByIdAndSession(id, session);
        if (session == null) {
            return new ResponseEntity<>("Session ID is missing or invalid", HttpStatus.UNAUTHORIZED);
        } else if (id == null || existingTransaction != null) {
            return new ResponseEntity<>("Invalid input given", HttpStatus.METHOD_NOT_ALLOWED);
        }
        transaction.setSession(session);
        if (categories != null) {
            categories.removeIf(c -> (c.getId() == null && c.getName() == null)) ;
            for (Category c : categories) {
                if (c.getId() == null || c.getName() == null || !categoryRepository.exists(c.getId())) {
                    return new ResponseEntity<>("Invalid input given", HttpStatus.METHOD_NOT_ALLOWED);
                } else {
                    c.addTransaction(transaction);
                    categoryRepository.save(c);
                }
            }
        }
        transaction.setCategory(categories);
        System.out.println(id + " " + date + " " + amount + " " + iban + " " + type + " " + categories);
        transactionRepository.save(transaction);
        session.addTransaction(transaction);
        sessionRepository.save(session);
        return new ResponseEntity<>("Successful operation", HttpStatus.CREATED);
    }
}
