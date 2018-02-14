package nl.ing.honours.transactions;

import nl.ing.honours.categories.Category;
import nl.ing.honours.categories.CategoryRepository;
import nl.ing.honours.exceptions.InvalidInputException;
import java.util.ArrayList;
import java.util.List;

public class TransactionFunctions {

    public static Long parseId(String idString) throws InvalidInputException {
        try {
            Long id = Long.parseLong(idString);
            if (id < 0) {
                throw new InvalidInputException("Id can't be negative!");
            }
            return id;
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Couldn't parse id!");
        }
    }

    public static List<Category> checkCategories(Transaction transaction, CategoryRepository categoryRepository) throws InvalidInputException {
        List<Category> categories = transaction.getCategory();
        List<Category> results = new ArrayList<>();
        if (categories != null) {
            categories.removeIf(c -> (c == null || c.getId() == null && c.getName() == null)) ;
            for (Category c : categories) {
                Category savedCategory = categoryRepository.findByIdAndSession(c.getId(), transaction.getSession());
                if (c.getId() == null || c.getName() == null || savedCategory == null ||
                        !savedCategory.getName().equals(c.getName())) {
                    throw new InvalidInputException("Invalid or unknown category specified!");
                } else {
                    results.add(savedCategory);
                }
            }
        }
        return results;
    }
}
