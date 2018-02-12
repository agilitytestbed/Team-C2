package nl.ing.honours.categories;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "Category")
@Entity
public class Category implements Serializable {

    @Id
    @Column(insertable = false, updatable = false, nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;

    public Category() {

    }

    public Category(Long id, String sessionId, String name) {

    }

    @Override
    public String toString() {
        return this.id + " " + this.name;
    }

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
}
