package nl.ing.honours.transactions;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import nl.ing.honours.categories.Category;

import java.io.IOException;
import java.util.List;

public class CustomCategorySerializer extends JsonSerializer<List<Category>> {

    @Override
    public void serialize(List<Category> categories, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        try {
            if (categories.size() > 0) {
                Category category = categories.get(0);
                jsonGenerator.writeStartObject();
                jsonGenerator.writeNumberField("id", category.getId());
                jsonGenerator.writeStringField("name", category.getName());
                jsonGenerator.writeEndObject();
            } else {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeEndObject();
            }
        } catch (Exception e) {
            System.out.println("!!!" + e.toString());
        }
    }
}
