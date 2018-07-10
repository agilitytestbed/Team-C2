package nl.utwente.ing.session;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "Session")
@JsonPropertyOrder({"id"})
public class Session implements Serializable {

    @Id
    private String id;

    private Float balance;

    public Session() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public void addToBalance(Float amount) {
        this.balance += amount;
    }
}
