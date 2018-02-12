package nl.ing.honours.categories;

import nl.ing.honours.AutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;


@RestController
@Import(AutoConfiguration.class)
@RequestMapping(value = "/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getCategories() {
        // 200
        return new ResponseEntity<>(categoryRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createCategory(@RequestBody Category category) {
        if (!categoryRepository.exists(category.getId())) {
            // 201
            categoryRepository.save(category);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            // 405
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @RequestMapping(value = "/{categoryId}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getCategory(@PathVariable Long categoryId) {
        if (categoryRepository.exists(categoryId)) {
            // 200
            return new ResponseEntity<>(categoryRepository.findOne(categoryId), HttpStatus.OK);
        } else {
            // 404
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{categoryId}", method = RequestMethod.PUT, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity updateCategory(@PathVariable Long categoryId, @RequestBody Category newCategory) {
        if (categoryRepository.exists(categoryId)) {
            // 200
            Category oldCategory = categoryRepository.findOne(categoryId);
            oldCategory.setName(newCategory.getName());
            categoryRepository.save(oldCategory);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            // 404
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{categoryId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteCategory(@PathVariable Long categoryId) {
        if (categoryRepository.exists(categoryId)) {
            // 204
            categoryRepository.delete(categoryId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            // 404
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
