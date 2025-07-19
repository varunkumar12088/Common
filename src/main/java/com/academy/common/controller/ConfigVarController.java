package com.academy.common.controller;

import com.academy.common.entity.ConfigVar;
import com.academy.common.service.impl.ConfigVarServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/config-vars")
public class ConfigVarController {


    @Autowired
    private ConfigVarServiceImpl configVarService;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> saveConfigVar(@RequestBody ConfigVar configVar) {
        configVar.setEncrypted(false);
       configVarService.save(configVar);
        return ResponseEntity.ok("Success");
    }

    @RequestMapping(value = "/encrypted", method = RequestMethod.POST)
    public ResponseEntity<?> saveEncryptedConfigVar(@RequestBody ConfigVar configVar) {
        configVar.setEncrypted(true);
        configVarService.save(configVar);
        return ResponseEntity.ok("Success");
    }
}
