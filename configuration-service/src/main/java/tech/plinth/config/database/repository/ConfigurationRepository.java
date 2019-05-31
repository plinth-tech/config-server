package tech.plinth.config.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.plinth.config.database.model.Configuration;

import java.util.Optional;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
    Optional<Configuration> findTopByPlatformOrderByVersionDesc(String platformId);

    Optional<Configuration> findByPlatformAndVersion(String configurationPlatform, Long configurationVersion);
}
