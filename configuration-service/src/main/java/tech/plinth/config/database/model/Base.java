package tech.plinth.config.database.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonNodeStringType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Entity
@Table(name = "base")
@TypeDef(name = "json-node", typeClass = JsonNodeStringType.class)
public class Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id", updatable = false)
    private Long id;

    @NotNull(message = "Version need to be defined")
    @Column(name = "version")
    private Long version;

    @NotNull(message = "No data to create a new version")
    @Type(type = "json-node")
    @Column(name = "data_json", columnDefinition = "json")
    private JsonNode dataJson;

    @Column(name = "creation_date", updatable = false)
    private OffsetDateTime creationDate;

    public Base() {}

    public Base(Long version, JsonNode dataJson) {
        this.version = version;
        this.dataJson = dataJson;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public JsonNode getDataJson() {
        return dataJson;
    }

    public void setDataJson(JsonNode dataJson) {
        this.dataJson = dataJson;
    }

    public OffsetDateTime getCreationDate() {
        return creationDate;
    }


}
