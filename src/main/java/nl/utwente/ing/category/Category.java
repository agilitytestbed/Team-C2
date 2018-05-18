package nl.utwente.ing.category;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import nl.utwente.ing.exceptions.InvalidInputException;
import nl.utwente.ing.session.Session;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;

@Entity
@Table(name = "Category")
@JsonPropertyOrder({"id", "name"})
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = Category.Deserializer.class)
@JsonSerialize(using = Category.Serializer.class)
public class Category implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    @JsonIgnore
    private Session session;

    @JsonCreator
    public Category(@JsonProperty("id") Long id) {
        this.id = id;
    }

    @JsonCreator
    public Category(@JsonProperty("name") String name) {
        this.name = name;
    }


    private Category() {

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

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public static class Deserializer extends StdDeserializer<Category> {
        public Deserializer() {
            this(null);
        }

        Deserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public Category deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
            JsonNode node = jp.getCodec().readTree(jp);
            if (node.has("name") && (node.has("id") || node.has("category_id"))) {
                throw new InvalidInputException();
            } else if (node.has("name")) {
                String name = node.get("name").asText();
                return new Category(name);
            } else if (node.has("id")) {
                Long id = node.get("id").asLong();
                return new Category(id);
            } else if (node.has("category_id")){
                Long id = node.get("category_id").asLong();
                return new Category(id);
            } else {
                throw new InvalidInputException();
            }
        }
    }

    public static class Serializer extends StdSerializer<Category> {

        public Serializer() {
            this(null);
        }

        Serializer(Class<Category> c) {
            super(c);
        }

        @Override
        public void serialize(
                Category value, JsonGenerator jgen, SerializerProvider provider)
                throws IOException, JsonProcessingException {
            jgen.writeStartObject();
            jgen.writeNumberField("id", value.id);
            jgen.writeStringField("name", value.name);
            jgen.writeEndObject();
        }
    }
}