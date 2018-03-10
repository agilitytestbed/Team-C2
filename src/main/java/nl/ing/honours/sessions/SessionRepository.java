package nl.ing.honours.sessions;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Session findBySessionId(Long sessionId);
}
