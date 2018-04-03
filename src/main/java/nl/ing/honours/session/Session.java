package nl.ing.honours.session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import nl.ing.honours.category.Category;
import nl.ing.honours.transaction.Transaction;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Session")
@JsonPropertyOrder({"id"})
public class Session implements Serializable {

    @Id
    private String id;

    @OneToMany(mappedBy = "session")
    @JsonIgnore
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "session")
    @JsonIgnore
    private List<Category> categories;

    public Session() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
