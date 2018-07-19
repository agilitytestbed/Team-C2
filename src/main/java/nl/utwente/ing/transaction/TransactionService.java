package nl.utwente.ing.transaction;

import nl.utwente.ing.category.Category;
import nl.utwente.ing.session.Session;
import nl.utwente.ing.session.SessionRepository;
import nl.utwente.ing.session.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final SessionRepository sessionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, SessionRepository sessionRepository) {
        this.transactionRepository = transactionRepository;
        this.sessionRepository = sessionRepository;
    }

    public List<Transaction> findBySession(Session session) {
        return transactionRepository.findBySession(session);
    }

    public Transaction findBySessionAndId(Session session, Long id) {
        return transactionRepository.findBySessionAndId(session, id);
    }

    public List<Transaction> findByCategoryAndSession(Category category, Session session) {
        return transactionRepository.findByCategoryAndSession(category, session);
    }

    public Transaction create(Transaction data) {
        Session session = data.getSession();
        if (data.getType() == Transaction.Type.deposit) {
            session.addToBalance(data.getAmount());
        } else {
            session.addToBalance(-data.getAmount());
        }
        sessionRepository.save(session);
        return transactionRepository.save(data);
    }

    public Transaction updateBySessionAndId(Transaction data, Session session, Long id) {
        Transaction transaction = transactionRepository.findBySessionAndId(session, id);
        Float oldAmount = transaction.getAmount();
        Float newAmount = data.getAmount();
        if (transaction.getType() == Transaction.Type.deposit) {
            session.addToBalance(-(oldAmount - newAmount));
        } else {
            session.addToBalance(oldAmount - newAmount);
        }
        transaction.setDate(data.getDate());
        transaction.setAmount(data.getAmount());
        transaction.setExternalIBAN(data.getExternalIBAN());
        transaction.setType(data.getType());
        sessionRepository.save(session);
        return transactionRepository.save(transaction);
    }

    public void deleteBySessionAndId(Session session, Long id) {
        Transaction transaction = transactionRepository.findBySessionAndId(session, id);
        if (transaction.getType() == Transaction.Type.deposit) {
            session.addToBalance(-transaction.getAmount());
        } else {
            session.addToBalance(transaction.getAmount());
        }
        sessionRepository.save(session);
        transactionRepository.delete(transaction);
    }

    public Transaction assignCategoryBySessionAndId(Category category, Session session, Long id) {
        Transaction transaction = transactionRepository.findBySessionAndId(session, id);
        transaction.setCategory(category);
        return transactionRepository.save(transaction);
    }
}
