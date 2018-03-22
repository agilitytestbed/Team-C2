package nl.ing.honours.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Session findById(Long id);
}
