package nl.ing.honours.categories;

import nl.ing.honours.transactions.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<TransactionCategory, Long> {

}
