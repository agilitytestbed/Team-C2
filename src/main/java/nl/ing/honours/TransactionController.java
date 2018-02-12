package nl.ing.honours;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;

import static org.springframework.util.MimeTypeUtils.*;

@RestController
@Import({AutoConfiguration.class})
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Transaction> getTransactions(@RequestParam("offset") String offeset, @RequestParam("limit") String limit) {
        return transactionRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = TEXT_PLAIN_VALUE)
    public String createTransaction(@RequestBody Transaction transaction) {
        Long id = transaction.getId();
        Date date = transaction.getDate();
        Double amount = transaction.getAmount();
        String iban = transaction.getIban();
        String type = transaction.getType();
        TransactionCategory category = transaction.getCategory();
        Long cat_id = category.getId();
        String cat_name = category.getName();

        System.out.println(id + " " + date + " " + amount + " " + iban + " " + type + " " + cat_id + " " + cat_name);
        if (categoryRepository.findOne(cat_id) == null) {
            categoryRepository.save(category);
        }

        transactionRepository.save(transaction);
        return transaction.toString();
    }
}
