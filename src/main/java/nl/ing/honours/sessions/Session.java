package nl.ing.honours.sessions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import nl.ing.honours.categories.Category;
import nl.ing.honours.transactions.Transaction;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Table(name = "Session")
@Entity
@JsonPropertyOrder({"session_id"})
public class Session implements Serializable {

    @JsonProperty(value = "session_id", required = true)
    @Id
    private Long sessionId;

    @JsonIgnore
    @OneToMany
    private List<Transaction> transactions;

    @JsonIgnore
    @OneToMany
    private List<Category> categories;

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public void removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void addCategory(Category category) {
        this.categories.add(category);
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
    }

    public Session() {

    }

    public Session(Long sessionId, List<Transaction> transactions, List<Category> categories) {
        this.sessionId = sessionId;
        this.transactions = transactions;
        this.categories = categories;
    }

}
