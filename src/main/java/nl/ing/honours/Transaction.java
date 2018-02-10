package nl.ing.honours;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Table(name = "Transaction")
@Entity
public class Transaction implements Serializable {

    @Id
    @NotBlank
    private Long id;

    @Id
    @NotBlank
    private String sessionId;

    @NotBlank
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date date;

    @NotBlank
    private Double amount;

    private String externalIban;

    @NotBlank
    private String type;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "id"),
            @JoinColumn(name = "sessionId")
    })
    private TransactionCategory category;

    public Transaction() {

    }

    public Transaction(Long id, String sessionId, Date Date, Double amount, String externalIban, String type,
                       TransactionCategory category) {

    }
}


