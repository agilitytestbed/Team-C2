package nl.utwente.ing.transaction;

import nl.utwente.ing.category.Category;
import nl.utwente.ing.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> findBySession(Session session) {
        return this.transactionRepository.findBySession(session);
    }

    public List<Transaction> findByCategoryAndSession(Category category, Session session) {
        return this.transactionRepository.findByCategoryAndSession(category, session);
    }

    public Transaction create(Transaction data) {
        return transactionRepository.save(data);
    }

    public Transaction findBySessionAndId(Session session, Long id) {
        return this.transactionRepository.findBySessionAndId(session, id);
    }

    public Transaction updateBySessionAndId(Transaction data, Session session, Long id) {
        Transaction transaction = this.transactionRepository.findBySessionAndId(session, id);
        transaction.setDate(data.getDate());
        transaction.setAmount(data.getAmount());
        transaction.setExternalIBAN(data.getExternalIBAN());
        transaction.setType(data.getType());
        return this.transactionRepository.save(transaction);
    }

    public void deleteBySessionAndId(Session session, Long id) {
        Transaction transaction = transactionRepository.findBySessionAndId(session, id);
        transactionRepository.delete(transaction);
    }

    public Transaction assignCategoryBySessionAndId(Category category, Session session, Long id) {
        Transaction transaction = this.transactionRepository.findBySessionAndId(session, id);
        transaction.setCategory(category);
        return this.transactionRepository.save(transaction);
    }
}
