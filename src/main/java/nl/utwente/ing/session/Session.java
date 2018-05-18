package nl.utwente.ing.session;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import nl.utwente.ing.category.Category;
import nl.utwente.ing.transaction.Transaction;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "Session")
@JsonPropertyOrder({"id"})
public class Session implements Serializable {

    @Id
    private String id;

    public Session() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
