package nl.utwente.ing.savingsGoal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import nl.utwente.ing.session.Session;

import javax.persistence.*;

@Entity
@Table(name = "Transaction")
@JsonPropertyOrder({"id", "name", "goal", "savePerMonth", "minBalanceRequired", "balance"})
public class SavingsGoal {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private Float goal;

    private Float savePerMonth;

    private Float minBalanceRequired;

    private Float balance;

    @ManyToOne
    @JsonIgnore
    private Session session;

    @JsonCreator
    public SavingsGoal(@JsonProperty(value = "name", required = true) String name,
                       @JsonProperty(value = "goal", required = true) Float goal,
                       @JsonProperty(value = "savePerMonth", required = true) Float savePerMonth,
                       @JsonProperty(value = "minBalanceRequired") Float minBalanceRequired) {
        this.name = name;
        this.goal = goal;
        this.savePerMonth = savePerMonth;
        if (minBalanceRequired != null) {
            this.minBalanceRequired = minBalanceRequired;
        } else {
            this.minBalanceRequired = 0f;
        }
        this.balance = 0f;
    }

    public SavingsGoal() {}

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

    public Float getGoal() {
        return goal;
    }

    public void setGoal(Float goal) {
        this.goal = goal;
    }

    public Float getSavePerMonth() {
        return savePerMonth;
    }

    public void setSavePerMonth(Float savePerMonth) {
        this.savePerMonth = savePerMonth;
    }

    public Float getMinBalanceRequired() {
        return minBalanceRequired;
    }

    public void setMinBalanceRequired(Float minBalanceRequired) {
        this.minBalanceRequired = minBalanceRequired;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
