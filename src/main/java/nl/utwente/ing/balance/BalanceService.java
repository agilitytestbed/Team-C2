package nl.utwente.ing.balance;

import nl.utwente.ing.exceptions.InvalidInputException;
import nl.utwente.ing.session.Session;
import nl.utwente.ing.transaction.Transaction;
import nl.utwente.ing.transaction.TransactionRepository;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.lang.Float.max;
import static java.lang.Float.min;

@Service
public class BalanceService {

    private final TransactionRepository transactionRepository;

    public BalanceService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Candlestick> generateHistory(Session session, String interval, int intervals) {
        DateTime dateTime = new DateTime();
        Float balance = session.getBalance();
        LinkedList<Candlestick> history = new LinkedList<>();
        for (int i = 0; i < intervals; i++) {
            DateTime newDate;
            switch (interval) {
                case "hour":  newDate = dateTime.minusHours(1);
                              break;
                case "day":   newDate = dateTime.minusDays(1);
                              break;
                case "week":  newDate = dateTime.minusWeeks(1);
                              break;
                case "month": newDate = dateTime.minusMonths(1);
                              break;
                case "year":  newDate = dateTime.minusYears(1);
                              break;
                default: throw new InvalidInputException();
            }
            List<Transaction> intervalTransactions =
                    this.transactionRepository.findTransactionsBySessionAndDateBetweenOrderByDateDescIdDesc(
                            session, newDate.toDate(), dateTime.toDate());
            Float high = balance;
            Float low = balance;
            Float volume = 0F;
            Float open = balance;
            for (Transaction t : intervalTransactions) {
                volume += t.getAmount();
                if (t.getType() == Transaction.Type.deposit) {
                    open -= t.getAmount();
                } else {
                    open += t.getAmount();
                }
                high = max(high, open);
                low = min(low, open);
            }
            history.addFirst(new Candlestick(open, balance, high, low, volume, newDate.toDate()));
            dateTime = newDate;
            balance = open;
        }
        return history;
    }

}
