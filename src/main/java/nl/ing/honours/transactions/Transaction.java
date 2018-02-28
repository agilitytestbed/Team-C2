package nl.ing.honours.transactions;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nl.ing.honours.sessions.Session;
import nl.ing.honours.categories.Category;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "Transaction")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "date", "amount", "external-iban", "type", "category"})
@Entity
public class Transaction implements Serializable {

    @Id
    @GeneratedValue
    @Column(insertable = false, updatable = false)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date date;

    private Double amount;

    @JsonProperty(value = "external-iban")
    private String iban;

    private String type;

    @ManyToMany
    @JsonSerialize(using = CustomCategorySerializer.class)
    private List<Category> category;

    @ManyToOne
    @JsonIgnore
    private Session session;

    public Transaction() {

    }

    public Transaction(Long id, Session sessionId, Date Date, Double amount, String externalIban, String type,
                       Category category) {
        this.id = id;
        this.session = sessionId;
        this.date = Date;
        this.amount = amount;
        this.iban = externalIban;
        this.type = type;
        this.category = new ArrayList<Category>();
        this.category.add(category);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}


