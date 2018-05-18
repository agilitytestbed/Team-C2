package nl.utwente.ing.categoryRule;

import com.fasterxml.jackson.annotation.*;
import nl.utwente.ing.session.Session;
import nl.utwente.ing.transaction.Transaction.Type;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "CategoryRule")
@JsonPropertyOrder({"id", "description", "iBAN", "type", "category_id", "applyOnHistory"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryRule implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String description;

    @ManyToOne
    @JsonIgnore
    private Session session;

    private String iBAN;

    private Type type;

    private int category_id;

    private boolean applyOnHistory;

    public CategoryRule() {}

    @JsonCreator
    public CategoryRule(@JsonProperty("description") String description, @JsonProperty("iBAN") String iBAN, @JsonProperty("type") Type type,
                        @JsonProperty("category_id") int category_id, @JsonProperty("applyOnHistory") boolean applyOnHistory) {
        this.description = description;
        this.iBAN = iBAN;
        this.type = type;
        this.category_id = category_id;
        this.applyOnHistory = applyOnHistory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String getiBAN() {
        return iBAN;
    }

    public void setiBAN(String iBAN) {
        this.iBAN = iBAN;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public boolean isApplyOnHistory() {
        return applyOnHistory;
    }

    public void setApplyOnHistory(boolean applyOnHistory) {
        this.applyOnHistory = applyOnHistory;
    }
}