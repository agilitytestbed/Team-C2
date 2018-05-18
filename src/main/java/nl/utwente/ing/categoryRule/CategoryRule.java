package nl.utwente.ing.categoryRule;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.sun.istack.internal.Nullable;
import nl.utwente.ing.session.Session;
import nl.utwente.ing.transaction.Transaction.Type;

import javax.persistence.*;
import java.io.IOException;
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

    @Nullable
    @JsonSerialize(nullsUsing = NullTypeSerializer.class)
    private Type type;

    private Long category_id;

    private boolean applyOnHistory;

    public CategoryRule() {}

    @JsonCreator
    public CategoryRule(@JsonProperty("description") String description, @JsonProperty("iBAN") String iBAN, @JsonProperty("type") String type,
                        @JsonProperty("category_id") Long category_id, @JsonProperty("applyOnHistory") boolean applyOnHistory) {
        this.description = description;
        this.iBAN = iBAN;
        if (!type.equals("")) {
            this.type = Type.valueOf(type);
        } else {
            this.type = null;
        }
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

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    public boolean isApplyOnHistory() {
        return applyOnHistory;
    }

    public void setApplyOnHistory(boolean applyOnHistory) {
        this.applyOnHistory = applyOnHistory;
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

