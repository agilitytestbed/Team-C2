package nl.ing.honours.category;

import nl.ing.honours.session.Session;
import nl.ing.honours.session.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @RequestMapping(value = "", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getCategories(@RequestHeader(name = "X-session-ID", required = false) Long sessionId) {
        Session session = sessionRepository.findById(sessionId);
        if (session == null) {
            return new ResponseEntity<>("Session ID is missing or invalid", HttpStatus.UNAUTHORIZED);
        }
        // 200
        return new ResponseEntity<>(categoryRepository.findAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createCategory(@RequestHeader(name = "X-session-ID", required = false) Long sessionId,
                                         @RequestBody Category category) {
        Session session = sessionRepository.findById(sessionId);
        if (session == null) {
            return new ResponseEntity<>("Session ID is missing or invalid", HttpStatus.UNAUTHORIZED);
        } else {
            category.setSession(session);
        }
        if (!categoryRepository.exists(category.getId())) {
            // 201
            categoryRepository.save(category);
            session.getCategories().add(category);
            sessionRepository.save(session);
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
