package nl.utwente.ing.transaction;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.sun.istack.internal.Nullable;
import nl.utwente.ing.category.Category;
import nl.utwente.ing.categoryRule.CategoryRule;
import nl.utwente.ing.exceptions.InvalidInputException;
import nl.utwente.ing.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.datetime.DateFormatter;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;


@Entity
@Table(name = "Transaction")
@JsonPropertyOrder({"id", "date", "amount", "description", "externalIBAN", "type", "category"})
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

    @JsonSerialize(nullsUsing = Transaction.NullTypeSerializer.class)
    private String description;

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
    public Transaction(@JsonProperty(value = "date", required = true) Date date,
                       @JsonProperty(value = "amount", required = true) Float amount,
                       @JsonProperty(value = "description") String description,
                       @JsonProperty(value = "externalIBAN", required = true) String externalIBAN,
                       @JsonProperty(value = "type", required = true) Type type) {
        this.date = date;
        this.amount = amount;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public static class NullTypeSerializer extends StdSerializer<Object> {

        public NullTypeSerializer() {
            this(null);
        }

        NullTypeSerializer(Class<Object> o) {
            super(o);
        }

        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString("");
        }
    }

}


