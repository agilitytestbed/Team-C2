package nl.utwente.ing.categoryRule;

import nl.utwente.ing.session.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRuleRepository extends JpaRepository<CategoryRule, Long> {

    List<CategoryRule> findBySession(Session session);

    CategoryRule findBySessionAndId(Session session, Long id);

}
