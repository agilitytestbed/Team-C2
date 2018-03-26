package nl.ing.honours.category;

import nl.ing.honours.exceptions.InvalidInputException;
import nl.ing.honours.exceptions.ResourceNotFoundException;
import nl.ing.honours.session.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    private final SessionService sessionService;

    private final CategoryService categoryService;

    public CategoryController(SessionService sessionService, CategoryService categoryService) {
        this.sessionService = sessionService;
        this.categoryService = categoryService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getCategories() {
        List<Category> categories = this.categoryService.findBySession(this.sessionService.getCurrent());
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createCategory(@RequestBody Category data) {
        if (data.getId() != null) {
            throw new InvalidInputException();
        }
        data.setSession(this.sessionService.getCurrent());
        Category category = this.categoryService.create(data);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getCategory(@PathVariable Long id) {
        Category category = this.categoryService.findBySessionAndId(this.sessionService.getCurrent(), id);
        if (category == null) {
            throw new ResourceNotFoundException();
        }
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity updateCategory(@PathVariable Long id, @RequestBody Category data) {
        if (data.getId() != null) {
            throw new InvalidInputException();
        }
        Category category = this.categoryService.findBySessionAndId(this.sessionService.getCurrent(), id);
        if (category == null) {
            throw new ResourceNotFoundException();
        }
        Category updatedCategory = this.categoryService.updateBySessionAndId(data, this.sessionService.getCurrent(), id);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteCategory(@PathVariable Long id) {
        Category category = this.categoryService.findBySessionAndId(sessionService.getCurrent(), id);
        if (category == null) {
            throw new ResourceNotFoundException();
        }
        this.categoryService.deleteBySessionAndId(this.sessionService.getCurrent(), id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
