package nl.utwente.ing.categoryRule;

import nl.utwente.ing.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryRuleService {

    private final CategoryRuleRepository categoryRuleRepository;

    @Autowired
    public CategoryRuleService(CategoryRuleRepository categoryRuleRepository) {
        this.categoryRuleRepository = categoryRuleRepository;
    }

    public List<CategoryRule> findBySession(Session session) {
        return this.categoryRuleRepository.findBySession(session);
    }

    public CategoryRule create(CategoryRule data) {
        return this.categoryRuleRepository.save(data);
    }

    public CategoryRule findBySessionAndId(Session session, Long id) {
        return this.categoryRuleRepository.findBySessionAndId(session, id);
    }

    public CategoryRule updateBySessionAndId(CategoryRule data, Session session, Long id) {
        CategoryRule categoryRule = this.categoryRuleRepository.findBySessionAndId(session, id);
        categoryRule.setDescription(data.getDescription());
        categoryRule.setiBAN(data.getiBAN());
        categoryRule.setType(data.getType());
        categoryRule.setCategory_id(data.getCategory_id());
        return this.categoryRuleRepository.save(categoryRule);
    }

    public void deleteBySessionAndId(Session session, Long id) {
        CategoryRule categoryRule = this.categoryRuleRepository.findBySessionAndId(session, id);
        this.categoryRuleRepository.delete(categoryRule);
    }
}