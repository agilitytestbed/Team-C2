package nl.utwente.ing.transaction;

import nl.utwente.ing.category.Category;
import nl.utwente.ing.category.CategoryService;
import nl.utwente.ing.exceptions.InvalidInputException;
import nl.utwente.ing.exceptions.ResourceNotFoundException;
import nl.utwente.ing.session.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final SessionService sessionService;

    private final TransactionService transactionService;

    private final CategoryService categoryService;

    public TransactionController(SessionService sessionService, TransactionService transactionService, CategoryService categoryService) {
        this.sessionService = sessionService;
        this.transactionService = transactionService;
        this.categoryService = categoryService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getTransactions(@RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
                                          @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
                                          @RequestParam(value = "category", required = false) String categoryName) {
        List<Transaction> transactions;
        if (categoryName == null) {
            transactions = this.transactionService.findBySession(this.sessionService.getCurrent());
        } else {
            Category category = this.categoryService.findBySessionAndName(this.sessionService.getCurrent(), categoryName);
            transactions = this.transactionService.findByCategoryAndSession(category, this.sessionService.getCurrent());
        }
        if ((limit + offset) > transactions.size()) {
            limit = transactions.size() - offset;
        }
        if (limit > transactions.size() - offset) {
            throw new InvalidInputException();
        }
        transactions = transactions.subList(offset, offset + limit);
        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity createTransaction(@RequestBody Transaction data) {
        data.setSession(this.sessionService.getCurrent());
        Transaction transaction = this.transactionService.create(data);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity getTransaction(@PathVariable(name = "id") Long id) {
        Transaction transaction = this.transactionService.findBySessionAndId(this.sessionService.getCurrent(), id);
        if (transaction != null) {
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity updateTransaction(@PathVariable(name = "id") Long id,
                                            @RequestBody Transaction data) {
        Transaction transaction = this.transactionService.findBySessionAndId(this.sessionService.getCurrent(), id);
        if (transaction == null) {
            throw new ResourceNotFoundException();
        }
        Transaction updatedTransaction = this.transactionService.updateBySessionAndId(data, this.sessionService.getCurrent(), id);
        return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteTransaction(@PathVariable(name = "id") Long id) {
        Transaction transaction = this.transactionService.findBySessionAndId(this.sessionService.getCurrent(), id);
        if (transaction == null) {
            throw new ResourceNotFoundException();
        }
        this.transactionService.deleteBySessionAndId(this.sessionService.getCurrent(), id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "{id}/category", method = RequestMethod.PATCH)
    public ResponseEntity assignCategory(@PathVariable(name = "id") Long id,
                                         @RequestBody Category data) {
        if (data.getId() == null) {
            throw new InvalidInputException();
        }
        Transaction transaction = this.transactionService.findBySessionAndId(this.sessionService.getCurrent(), id);
        if (transaction == null) {
            throw new ResourceNotFoundException();
        }
        Category category = this.categoryService.findBySessionAndId(this.sessionService.getCurrent(), data.getId());
        if (category == null) {
            throw new ResourceNotFoundException();
        }
        Transaction updatedTransaction = this.transactionService.assignCategoryBySessionAndId(category, this.sessionService.getCurrent(), id);
        return new ResponseEntity<>(updatedTransaction, HttpStatus.OK);
    }
}
