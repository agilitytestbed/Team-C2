package nl.ing.honours.transactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.gson.annotations.SerializedName;
import nl.ing.honours.sessions.Session;
import nl.ing.honours.categories.Category;
import nl.ing.honours.sessions.Session;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Table(name = "Transaction")
@Entity
public class Transaction implements Serializable {

    @Id
    @Column(insertable = false, updatable = false)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date date;

    private Double amount;

    @SerializedName("external-iban")
    @JsonProperty("external-iban")
    private String iban;

    private String type;

    @OneToMany
    @JsonDeserialize()
    private List<Category> category;

    @ManyToOne
    private Session session;

    public Transaction() {

    }

    public Transaction(Long id, String sessionId, Date Date, Double amount, String externalIban, String type,
                       Category category) {

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


