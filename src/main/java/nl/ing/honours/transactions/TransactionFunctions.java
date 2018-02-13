package nl.ing.honours.transactions;

import nl.ing.honours.categories.Category;
import nl.ing.honours.categories.CategoryRepository;
import nl.ing.honours.exceptions.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class TransactionFunctions {

    @Autowired
    private static CategoryRepository categoryRepository;

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

    public static List<Category> checkCategories(Transaction transaction) throws InvalidInputException {
        List<Category> categories = transaction.getCategory();
        if (categories != null) {
            categories.removeIf(c -> (c.getId() == null && c.getName() == null)) ;
            for (Category c : categories) {
                if (c.getId() == null || c.getName() == null || !categoryRepository.exists(c.getId())) {
                    throw new InvalidInputException("Invalid or unknown category specified!");
                } else {
                    Category savedCategory = categoryRepository.findOne(c.getId());
                    savedCategory.addTransaction(transaction);
                    categoryRepository.save(savedCategory);
                }
            }
            return categories;
        } else {
            return new ArrayList<>();
        }
    }
}
