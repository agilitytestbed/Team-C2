package nl.ing.honours.categories;

import nl.ing.honours.sessions.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {

    Category findByIdAndSession(Long id, Session session);

    Category findByNameIgnoreCaseAndSession(String name, Session session);

}
