package tech.plinth.config.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
        return configurationDelegate.getVersion(version);
    }

}
