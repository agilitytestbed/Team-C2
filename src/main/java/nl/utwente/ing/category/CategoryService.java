package nl.utwente.ing.category;

import nl.utwente.ing.session.Session;
import nl.utwente.ing.transaction.Transaction;
import nl.utwente.ing.transaction.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;


    @Autowired
    public CategoryService(CategoryRepository categoryRepository, TransactionRepository transactionRepository) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<Category> findBySession(Session session) {
        return this.categoryRepository.findBySession(session);
    }

    public Category create(Category data) {
        return this.categoryRepository.save(data);
    }

    public Category findBySessionAndId(Session session, Long id) {
        return this.categoryRepository.findBySessionAndId(session, id);
    }

    public Category updateBySessionAndId(Category data, Session session, Long id) {
        Category category = this.categoryRepository.findBySessionAndId(session, id);
        category.setName(data.getName());
        return this.categoryRepository.save(category);
    }

    public void deleteBySessionAndId(Session session, Long id) {
        Category category = categoryRepository.findBySessionAndId(session, id);
        List<Transaction> transactions = transactionRepository.findByCategoryAndSession(category, session);
        for (Transaction transaction : transactions) {
            transaction.setCategory(null);
        }
        this.transactionRepository.save(transactions);
        this.categoryRepository.delete(category);
    }

    public Category findBySessionAndName(Session session, String name) {
        return categoryRepository.findBySessionAndName(session, name);
    }
}
