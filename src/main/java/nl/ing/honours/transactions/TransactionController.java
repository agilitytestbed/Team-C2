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

import java.util.ArrayList;
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
                                          @RequestParam(value = "offset", required = false) String offsetInput,
                                          @RequestParam(value = "limit", required = false) String limitInput,
                                          @RequestParam(value = "category", required = false) String categoryName) {
        Session session = sessionRepository.findFirstById(sessionId);
        if (session == null) {
            return new ResponseEntity<>("Session ID is missing or invalid", HttpStatus.UNAUTHORIZED);
        }
        List<Transaction> transactions;
        if (categoryName != null) {
            Category category = categoryRepository.findByName(categoryName);
            if (category != null) {
                transactions = category.getTransactions();
            } else {
                transactions = session.getTransactions();
            }
        } else {
            transactions = session.getTransactions();
        }
        Integer offset = 0;
        Integer limit = 20;
        if  (transactions.size() < 20) {
            limit = transactions.size();
        }
        try {
            if (offsetInput != null) {
                offset = Integer.parseInt(offsetInput);
            }
            if (limitInput != null) {
                limit = Integer.parseInt(limitInput);
            }
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid input given", HttpStatus.METHOD_NOT_ALLOWED);
        }

        if (offset > transactions.size()) {
            offset = transactions.size();
        } else if (offset < 0) {
            return new ResponseEntity<>("Invalid input given", HttpStatus.METHOD_NOT_ALLOWED);
        }
        if (limit > transactions.size() - offset) {
            limit = transactions.size() - offset;
        } else if (limit < 0) {
            return new ResponseEntity<>("Invalid input given", HttpStatus.METHOD_NOT_ALLOWED);
        }

        transactions.sort(Comparator.comparing(Transaction::getId));
        transactions = transactions.subList(offset, transactions.size());
        transactions = transactions.subList(0, limit);
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
        } else {
            transaction.setCategory(new ArrayList<Category>());
        }
        transaction.setCategory(categories);
        System.out.println(id + " " + date + " " + amount + " " + iban + " " + type + " " + categories);
        transactionRepository.save(transaction);
        session.addTransaction(transaction);
        sessionRepository.save(session);
        return new ResponseEntity<>("Successful operation", HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity findTransaction(@RequestHeader(name = "WWW_Authenticate", required = false) String sessionId,
                                          @PathVariable(name = "id") String idString) {
        Session session = sessionRepository.findFirstById(sessionId);
        if (session == null) {
            return new ResponseEntity<>("Session ID is missing or invalid", HttpStatus.UNAUTHORIZED);
        }
        Long id;
        try {
            id = Long.parseLong(idString);
            if (id < 0) {
                return new ResponseEntity<>("Invalid input given", HttpStatus.METHOD_NOT_ALLOWED);
            }
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid input given", HttpStatus.METHOD_NOT_ALLOWED);
        }
        Transaction transaction = transactionRepository.findOne(id);
        if (transaction != null) {
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Transaction not found", HttpStatus.NOT_FOUND);
        }
    }
}
