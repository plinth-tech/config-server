package tech.plinth.config.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import tech.plinth.config.delegate.ConfigurationDelegate;

@RestController
public class ConfigurationController {

    @Autowired
    private ConfigurationDelegate configurationDelegate;

    /**
     * This endpoint receive a json, this json will be a new version (a update) of base file
     * From the header come the platform, to be saved in db together with the json data
     * Will return the json saved
     *
     * @param dataJson
     * @return
     */
    @PostMapping("/config")
    public JsonNode createNewVersion(@RequestBody JsonNode dataJson) {
        return configurationDelegate.createNewVersion(dataJson);
    }

    @GetMapping("/config")
    public JsonNode getVersion(@RequestParam("version") Long version) throws JsonPatchException {
        if (version == null) {
            return configurationDelegate.getLastVersion();
        }
        else {
            return configurationDelegate.getVersion(version);
        }
    }

}
