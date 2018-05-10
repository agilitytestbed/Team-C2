package nl.ing.honours.transaction;

import com.fasterxml.jackson.annotation.*;
import nl.ing.honours.category.Category;
import nl.ing.honours.session.Session;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "Transaction")
@JsonPropertyOrder({"id", "date", "amount", "externalIBAN", "type", "category"})
public class Transaction implements Serializable {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date date;

    private Float amount;

    private String externalIBAN;

    private Type type;

    @ManyToOne
    @JsonIgnore
    private Category category;

    @ManyToOne
    @JsonIgnore
    private Session session;

    public enum Type {
        deposit,
        withdrawal
    }

    public Transaction() {}

    @JsonCreator
    public Transaction(@JsonProperty("date") Date date, @JsonProperty("amount") Float amount,
                       @JsonProperty("externalIBAN") String externalIBAN, @JsonProperty("type") Type type) {
        this.date = date;
        this.amount = amount;
        this.externalIBAN = externalIBAN;
        this.type = type;
    }

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonIgnore
    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getExternalIBAN() {
        return externalIBAN;
    }

    public void setExternalIBAN(String externalIBAN) {
        this.externalIBAN = externalIBAN;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @JsonProperty("category")
    public Category getCategory() {
        return category;
    }

    @JsonIgnore
    public void setCategory(Category category) {
        this.category = category;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}


