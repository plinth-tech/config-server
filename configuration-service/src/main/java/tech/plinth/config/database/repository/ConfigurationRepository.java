package tech.plinth.config.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.plinth.config.database.model.Configuration;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
    Configuration findTopByPlatformOrderByVersionDesc(String platformId);
}
