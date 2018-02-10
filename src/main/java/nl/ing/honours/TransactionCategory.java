package nl.ing.honours;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "Category")
@Entity
public class TransactionCategory implements Serializable {

    @Id
    @NotBlank
    private Long id;

    @Id
    @NotBlank
    private String sessionId;

    @NotBlank
    private String name;

    public TransactionCategory() {

    }

    private TransactionCategory(Long id, String sessionId, String name) {

    }
}
