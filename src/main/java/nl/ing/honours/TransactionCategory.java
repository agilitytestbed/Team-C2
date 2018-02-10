package nl.ing.honours;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "Category")
@Entity
public class TransactionCategory implements Serializable {

    @Id
    @NotBlank
    private Long id;

    @NotBlank
    private String name;

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
