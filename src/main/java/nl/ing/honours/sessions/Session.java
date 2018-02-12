package nl.ing.honours.sessions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    private List<Category> category;

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

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public Session() {

    }

    public Session(String sessionId) {
        this.session = sessionId;
    }

}
