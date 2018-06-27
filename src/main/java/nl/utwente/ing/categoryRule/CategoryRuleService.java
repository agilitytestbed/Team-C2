package nl.utwente.ing.categoryRule;

import nl.utwente.ing.category.Category;
import nl.utwente.ing.category.CategoryRepository;
import nl.utwente.ing.session.Session;
import nl.utwente.ing.transaction.Transaction;
import nl.utwente.ing.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryRuleService {

    private final CategoryRuleRepository categoryRuleRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public CategoryRuleService(CategoryRuleRepository categoryRuleRepository, CategoryRepository categoryRepository,
                               TransactionRepository transactionRepository) {
        this.categoryRuleRepository = categoryRuleRepository;
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<CategoryRule> findBySession(Session session) {
        return this.categoryRuleRepository.findBySession(session);
    }

    public CategoryRule create(CategoryRule data) {
        return this.categoryRuleRepository.save(data);
    }

    public CategoryRule findBySessionAndId(Session session, Long id) {
        return this.categoryRuleRepository.findBySessionAndId(session, id);
    }

    public CategoryRule updateBySessionAndId(CategoryRule data, Session session, Long id) {
        CategoryRule categoryRule = this.categoryRuleRepository.findBySessionAndId(session, id);
        categoryRule.setDescription(data.getDescription());
        categoryRule.setiBAN(data.getiBAN());
        categoryRule.setType(data.getType());
        categoryRule.setCategory_id(data.getCategory_id());
        return this.categoryRuleRepository.save(categoryRule);
    }

    public void deleteBySessionAndId(Session session, Long id) {
        CategoryRule categoryRule = this.categoryRuleRepository.findBySessionAndId(session, id);
        this.categoryRuleRepository.delete(categoryRule);
    }

    public Transaction applyCategoryRule(Transaction transaction) {
        List<CategoryRule> rules = this.categoryRuleRepository.findApplicableRules(transaction.getSession(),
                                                                                   transaction.getDescription(),
                                                                                   transaction.getExternalIBAN(),
                                                                                   transaction.getType());
        for (CategoryRule rule : rules) {
            Category category = this.categoryRepository.findBySessionAndId(rule.getSession(), rule.getCategory_id());
            if (category != null) {
                if (transaction.getCategory() != category) {
                    transaction.setCategory(category);
                }
                break;
            }
        }
        return transaction;
    }

    public void applyOnHistory(CategoryRule categoryRule) {
        List<Transaction> transactions = this.transactionRepository.findTransactionsByCategoryRule(categoryRule.getSession(),
                                                                                                   categoryRule.getDescription(),
                                                                                                   categoryRule.getiBAN(),
                                                                                                   categoryRule.getType());
        Category category = this.categoryRepository.findBySessionAndId(categoryRule.getSession(), categoryRule.getCategory_id());
        for (Transaction transaction : transactions) {
            transaction.setCategory(category);
            this.transactionRepository.save(transaction);
        }
    }
}