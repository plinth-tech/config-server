package tech.plinth.config.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.plinth.config.database.model.Base;

import java.util.Optional;

@Repository
public interface BaseRepository extends JpaRepository<Base, Long> {

    Optional<Base> findTopByOrderByVersionDesc();
}
