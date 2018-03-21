package nl.ing.honours.transactions;

import nl.ing.honours.AutoConfiguration;
import nl.ing.honours.categories.CategoryRepository;
import nl.ing.honours.exceptions.InvalidInputException;
import nl.ing.honours.sessions.Session;
import nl.ing.honours.sessions.SessionRepository;
import nl.ing.honours.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getTransactions(@RequestHeader(name = "WWW_Authenticate", required = false) Long sessionId,
                                          @RequestParam(value = "offset", required = false) String offsetInput,
                                          @RequestParam(value = "limit", required = false) String limitInput,
                                          @RequestParam(value = "category", required = false) String categoryName) {
        Session session = sessionRepository.findBySessionId(sessionId);
        if (session == null) {
            return new ResponseEntity<>("Session ID is missing or invalid", HttpStatus.UNAUTHORIZED);
        }
        List<Transaction> transactions;
        if (categoryName != null) {
            Category category = categoryRepository.findByNameIgnoreCaseAndSession(categoryName, session);
            if (category != null) {
                transactions = category.getTransactions();
            } else {
                return new ResponseEntity<>("Invalid input given", HttpStatus.METHOD_NOT_ALLOWED);
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
    public ResponseEntity createTransaction(@RequestHeader(name = "X-session-ID", required = false) Long sessionId,
                                            @RequestBody(required = false) Transaction transaction) {
        Long id = transaction.getId();
        Session session = sessionRepository.findBySessionId(sessionId);
        if (session == null) {
            return new ResponseEntity<>("Session ID is missing or invalid", HttpStatus.UNAUTHORIZED);
        }
        if (id != null) {
            Transaction existingTransaction = transactionRepository.findByIdAndSession(id, session);
            if (existingTransaction != null) {
                return new ResponseEntity<>("Invalid input given", HttpStatus.METHOD_NOT_ALLOWED);
            }
        }
        transaction.setSession(session);
        try {
            List<Category> verifiedCategory = TransactionFunctions.checkCategories(transaction, categoryRepository);
            transaction.setCategory(verifiedCategory);
        } catch (InvalidInputException e) {
            return new ResponseEntity<>("Invalid input given", HttpStatus.METHOD_NOT_ALLOWED);
        }
        transactionRepository.saveAndFlush(transaction);
        session.addTransaction(transaction);
        sessionRepository.save(session);
        return new ResponseEntity<>("Successful operation", HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity findTransaction(@RequestHeader(name = "X-session-ID", required = false) Long sessionId,
                                          @PathVariable(name = "id") String idString) {
        Session session = sessionRepository.findBySessionId(sessionId);
        if (session == null) {
            return new ResponseEntity<>("Session ID is missing or invalid", HttpStatus.UNAUTHORIZED);
        }
        Long id;
        try {
            id = TransactionFunctions.parseId(idString);
        } catch (InvalidInputException e) {
            return new ResponseEntity<>("Invalid input given", HttpStatus.METHOD_NOT_ALLOWED);
        }
        Transaction transaction = transactionRepository.findOne(id);
        if (transaction != null) {
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Transaction not found", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity updateTransaction(@RequestHeader(name = "X-session-ID", required = false) Long sessionId,
                                            @PathVariable(name = "id") String idString,
                                            @RequestBody(required = false) Transaction transaction) {
        Long bodyId = transaction.getId();
        Session session = sessionRepository.findBySessionId(sessionId);
        if (session == null) {
            return new ResponseEntity<>("Session ID is missing or invalid", HttpStatus.UNAUTHORIZED);
        } else {
            transaction.setSession(session);
        }
        Long headerId;
        try {
            headerId = TransactionFunctions.parseId(idString);
        } catch (InvalidInputException e) {
            return new ResponseEntity<>("Invalid input given", HttpStatus.METHOD_NOT_ALLOWED);
        }
        if (bodyId != null) {
            if (!Objects.equals(headerId, bodyId)) {
                return new ResponseEntity<>("Invalid input given", HttpStatus.METHOD_NOT_ALLOWED);
            }
        } else {
            transaction.setId(headerId);
        }
        // verify new categories exist
        try {
            List<Category> verifiedCategory = TransactionFunctions.checkCategories(transaction, categoryRepository);
            transaction.setCategory(verifiedCategory);
        } catch (InvalidInputException e) {
            return new ResponseEntity<>("Invalid input given", HttpStatus.METHOD_NOT_ALLOWED);
        }
        Transaction existingTransaction = transactionRepository.findByIdAndSession(transaction.getId(), session);
        if (existingTransaction != null) {
            for (Category c : existingTransaction.getCategory()) {
                if (!transaction.getCategory().contains(c)) {
                    c.removeTransaction(existingTransaction);
                    categoryRepository.saveAndFlush(c);
                }
            }
            for (Category c : transaction.getCategory()) {
                if (!existingTransaction.getCategory().contains(c)) {
                    c.addTransaction(transaction);
                    categoryRepository.saveAndFlush(c);
                }
            }
            transactionRepository.save(transaction);
        } else {
            return new ResponseEntity<>("Transaction not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Successful operation", HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTransaction(@RequestHeader(name = "X-session-ID", required = false) Long sessionId,
                                            @PathVariable(name = "id") String idString) {
        Session session = sessionRepository.findBySessionId(sessionId);
        if (session == null) {
            return new ResponseEntity<>("Session ID is missing or invalid", HttpStatus.UNAUTHORIZED);
        }
        Long headerId;
        try {
            headerId = TransactionFunctions.parseId(idString);
        } catch (InvalidInputException e) {
            return new ResponseEntity<>("Invalid input given", HttpStatus.METHOD_NOT_ALLOWED);
        }
        Transaction transaction = transactionRepository.findByIdAndSession(headerId, session);
        if (transaction != null) {
            session.removeTransaction(transaction);
            for (Category c : transaction.getCategory()) {
                c.removeTransaction(transaction);
                categoryRepository.saveAndFlush(c);
            }
            sessionRepository.saveAndFlush(session);
            transactionRepository.delete(transaction);
        } else {
            return new ResponseEntity<>("Transaction not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Resource deleted", HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "{id}/assigncategory", method = RequestMethod.POST)
    public ResponseEntity assignCategory(@RequestHeader(name = "X-session-ID", required = false) Long sessionId,
                                         @PathVariable(name = "id") String transactionInput,
                                         @RequestParam(value = "categoryId", required = false) String categoryInput) {
        Session session = sessionRepository.findBySessionId(sessionId);
        if (session == null) {
            return new ResponseEntity<>("Session ID is missing or invalid", HttpStatus.UNAUTHORIZED);
        }
        Long transactionId;
        Long categoryId;
        try {
            transactionId = TransactionFunctions.parseId(transactionInput);
            if (categoryInput != null) {
                categoryId = TransactionFunctions.parseId(categoryInput);
            } else {
                return new ResponseEntity<>("Invalid input given", HttpStatus.METHOD_NOT_ALLOWED);
            }
        } catch (InvalidInputException e) {
            return new ResponseEntity<>("Invalid input given", HttpStatus.METHOD_NOT_ALLOWED);
        }
        Transaction transaction = transactionRepository.findByIdAndSession(transactionId, session);
        Category category = categoryRepository.findByIdAndSession(categoryId, session);
        if (transaction != null && category != null) {
            transaction.getCategory().clear();
            transaction.addCategory(category);
            category.addTransaction(transaction);
            transactionRepository.save(transaction);
            categoryRepository.save(category);
        } else {
            return new ResponseEntity<>("Transaction or category not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Successful operation", HttpStatus.OK);
    }
}
