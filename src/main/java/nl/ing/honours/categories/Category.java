package nl.ing.honours.categories;

import nl.ing.honours.sessions.Session;
import nl.ing.honours.transactions.Transaction;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Table(name = "Category")
@Entity
public class Category implements Serializable {

    @Id
    @Column(name = "id", unique = true, nullable = false, insertable = true, updatable = true)
    private Long id;
    @Column(name = "name", unique = false, nullable = true, insertable = true, updatable = true)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Transaction> transactions;

    @ManyToOne
    private Session session;

    public Category() {

    }

    public Category(Long id, String sessionId, String name) {

    }

    @Override
    public String toString() {
        return this.id + " " + this.name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction) {this.transactions.add(transaction);}

    public void removeTransaction(Transaction transaction) {this.transactions.remove(transaction);}

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
