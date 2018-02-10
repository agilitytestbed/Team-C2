package nl.ing.honours;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<TransactionCategory, Long> {

}
