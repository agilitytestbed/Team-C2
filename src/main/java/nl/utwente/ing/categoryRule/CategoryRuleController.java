package nl.utwente.ing.categoryRule;

import nl.utwente.ing.category.Category;
import nl.utwente.ing.category.CategoryService;
import nl.utwente.ing.exceptions.InvalidInputException;
import nl.utwente.ing.session.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        if (data.isApplyOnHistory()) {
            // TODO apply categoryRule
        }
        CategoryRule categoryRule = this.categoryRuleService.create(data);
        return new ResponseEntity<>(categoryRule, HttpStatus.CREATED);
    }

}
