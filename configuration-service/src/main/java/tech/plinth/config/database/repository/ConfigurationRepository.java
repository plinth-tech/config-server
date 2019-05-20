package tech.plinth.config.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.plinth.config.database.model.Configuration;

import java.util.List;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, Long> {
    List<Configuration> findByPlatform(String platformId);

    Configuration findTopByPlatformOrderByVersionDesc(String platformId);
}