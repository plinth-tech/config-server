package tech.plinth.config.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.plinth.config.delegate.BaseDelegate;

@RestController
public class BaseController {

    @Autowired
    BaseDelegate baseDelegate;

    @GetMapping("/base")
    public JsonNode getBase() {
        return baseDelegate.getBase();
    }

}
