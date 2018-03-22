package nl.ing.honours.session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import nl.ing.honours.category.Category;
import nl.ing.honours.transaction.Transaction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Session")
@JsonPropertyOrder({"session_id"})
public class Session implements Serializable {

    @Id
    @JsonProperty(value = "session_id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @OneToMany(mappedBy = "session")
    @JsonIgnore
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "session")
    @JsonIgnore
    private List<Category> categories;

    public Session() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
