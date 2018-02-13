package nl.ing.honours.sessions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import nl.ing.honours.transactions.Transaction;
import nl.ing.honours.categories.Category;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table(name = "Session")
@Entity
@JsonIgnoreProperties(value = { "id" })
public class Session implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String session;

    @OneToMany
    private List<Transaction> transactions;

    @OneToMany
    private List<Category> categories;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSession_token() {
        return session;
    }

    public void setSession_token(String session_token) {
        this.session = session_token;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction) {this.transactions.add(transaction);}

    public void removeTransaction(Transaction transaction) {this.transactions.remove(transaction);}

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void addCategory(Category category) {this.categories.add(category);}

    public void removeCategory(Category category) {this.categories.remove(category);}

    public Session() {

    }

    public Session(String sessionId) {
        this.session = sessionId;
    }

}
