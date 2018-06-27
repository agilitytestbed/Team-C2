package nl.utwente.ing.categoryRule;

import nl.utwente.ing.category.Category;
import nl.utwente.ing.category.CategoryService;
import nl.utwente.ing.exceptions.InvalidInputException;
import nl.utwente.ing.exceptions.ResourceNotFoundException;
import nl.utwente.ing.session.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping(value = "/categoryRules")
public class CategoryRuleController {

    private final SessionService sessionService;

    private final CategoryService categoryService;

    private final CategoryRuleService categoryRuleService;

    public CategoryRuleController(SessionService sessionService, CategoryService categoryService, CategoryRuleService categoryRuleService) {
        this.sessionService = sessionService;
        this.categoryService = categoryService;
        this.categoryRuleService = categoryRuleService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getCategoryRules() {
        List<CategoryRule> categoryRuleList = this.categoryRuleService.findBySession(this.sessionService.getCurrent());
        return new ResponseEntity<>(categoryRuleList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity createCategoryRule(@RequestBody CategoryRule data) {
        Category category = this.categoryService.findBySessionAndId(this.sessionService.getCurrent(), data.getCategory_id());
        if (category == null) {
            throw new InvalidInputException();
        }
        data.setSession(this.sessionService.getCurrent());
        CategoryRule categoryRule = this.categoryRuleService.create(data);
        if (categoryRule.isApplyOnHistory()) {
            this.categoryRuleService.applyOnHistory(categoryRule);
        }
        return new ResponseEntity<>(categoryRule, HttpStatus.CREATED);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ResponseEntity findCategoryRule(@PathVariable(name = "id") Long id) {
        CategoryRule categoryRule = this.categoryRuleService.findBySessionAndId(this.sessionService.getCurrent(), id);
        if (categoryRule != null) {
            return new ResponseEntity<>(categoryRule, HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public ResponseEntity updateCategoryRule(@PathVariable(name = "id") Long id,
                                             @RequestBody CategoryRule data) {
        CategoryRule categoryRule = this.categoryRuleService.findBySessionAndId(this.sessionService.getCurrent(), id);
        Category category = this.categoryService.findBySessionAndId(this.sessionService.getCurrent(), data.getCategory_id());
        if (categoryRule == null) {
            throw new ResourceNotFoundException();
        } else if (category == null) {
            throw new InvalidInputException();
        }
        CategoryRule updatedCategoryRule = this.categoryRuleService.updateBySessionAndId(data, this.sessionService.getCurrent(), id);
        return new ResponseEntity<>(updatedCategoryRule, HttpStatus.OK);
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteCategoryRule(@PathVariable(name = "id") Long id) {
        CategoryRule categoryRule = this.categoryRuleService.findBySessionAndId(this.sessionService.getCurrent(), id);
        if (categoryRule == null) {
            throw new ResourceNotFoundException();
        }
        this.categoryRuleService.deleteBySessionAndId(this.sessionService.getCurrent(), id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
