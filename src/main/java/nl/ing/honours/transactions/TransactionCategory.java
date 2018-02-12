package nl.ing.honours.transactions;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Table(name = "Category")
@Entity
public class TransactionCategory implements Serializable {

    @Id
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<Transaction> transactions;

    public TransactionCategory() {

    }

    public TransactionCategory(Long id, String sessionId, String name) {

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
}
