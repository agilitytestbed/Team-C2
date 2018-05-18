package nl.utwente.ing.categoryRule;

import nl.utwente.ing.session.SessionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/categoryRules")
public class CategoryRuleController {

    private final SessionService sessionService;

    private final CategoryRuleService categoryRuleService;

    public CategoryRuleController(SessionService sessionService, CategoryRuleService categoryRuleService) {
        this.sessionService = sessionService;
        this.categoryRuleService = categoryRuleService;
    }

}
