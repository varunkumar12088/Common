package com.academy.common.controller;

import com.academy.common.entity.ConfigVar;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/config-vars")
public class ConfigVarController {

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> saveConfigVar(@RequestBody ConfigVar configVar) {
        // Logic to save the config variable
        return ResponseEntity.ok("Success");
    }
}
