package com.academy.common.service.impl;


import com.academy.common.constant.CommonConstant;
import com.academy.common.entity.ConfigVar;
import com.academy.common.exception.DataLayerException;
import com.academy.common.exception.DataNotFoundException;
import com.academy.common.repository.ConfigVarRepository;
import com.academy.common.service.ConfigVarService;
import com.academy.common.util.AESUtil;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ConfigVarServiceImpl implements ConfigVarService<ConfigVar, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigVarServiceImpl.class);

    @Autowired
    private ConfigVarRepository configVarRepository;
    private String encryptionKey;

    @PostConstruct
    public void init(){
        Optional<ConfigVar> configVarOptional = configVarRepository.findById(CommonConstant.ENCRYPTION_KEY_ID);
        if(configVarOptional.isEmpty()){
            this.encryptionKey = CommonConstant.ENCRYPTION_KEY;
            return;
        }
        this.encryptionKey = (String) configVarOptional.get().getValue();
    }

    @Override
    public void save(ConfigVar configVar) {
        LOGGER.debug("Saving config variable: {}", configVar);
        validation(configVar);
        if (configVar.isEncrypted()) {
            try {
                configVar.setValue(AESUtil.encrypt((String) configVar.getValue(), this.encryptionKey));
            } catch (Exception e) {
                LOGGER.error("Failed to encrypt value: {}", configVar.getValue(), e);
                throw new DataLayerException("Failed to encrypt value: " + configVar.getValue(), e);
            }
        }
        configVarRepository.save(configVar);
        LOGGER.debug("Config variable saved successfully: {}", configVar);
    }

    @Override
    public <T> T getConfigVar(String id) {
        LOGGER.debug("Fetching config variable with id: {}", id);
        Optional<ConfigVar> configVarOptional = configVarRepository.findById(id);
        if (configVarOptional.isPresent()) {
            LOGGER.debug("Config variable found: {}", configVarOptional.get());
            return (T) configVarOptional.get();
        }
        throw new DataNotFoundException("Configuration is not available at " + id);
    }

    @Override
    public <T> T getConfigVarValue(String id, Class<T> valueType) {
        return getConfigVarValue(id, valueType, null);
    }

    @Override
    public <T> T getConfigVarValue(String id, Class<T> valueType, T defaultValue) {
        ConfigVar configVar = this.getConfigVar(id);
        if(ObjectUtils.isNotEmpty(configVar) && ObjectUtils.isNotEmpty(configVar.getValue()) && valueType != null){
            try {
                return getValue(configVar, valueType);
            } catch (ClassCastException e) {
                LOGGER.error("Failed to case " + configVar.getValue() + " to " + valueType, e);
                throw new DataLayerException("Failed to case " + configVar.getValue() + " to " + valueType, e);
            }
        }
        return defaultValue;
    }

    private <T> T getValue(ConfigVar configVar, Class<T> valueType) {
        try {
            // If the value is encrypted, decrypt it before casting
            if (configVar.isEncrypted()) {
                return valueType.cast(AESUtil.decrypt((String)configVar.getValue(), this.encryptionKey));
            }
            return valueType.cast(configVar.getValue());
        } catch (Exception e) {
            LOGGER.error("Failed to decrypt or cast value: {}", configVar.getValue(), e);
            throw new DataLayerException("Failed to decrypt or cast value: " + configVar.getValue(), e);
        }
    }

    private void validation(ConfigVar configVar){
        if(configVar == null){
            throw new IllegalArgumentException("ConfigVar cannot be null");
        }
        if(StringUtils.isBlank(configVar.getId())){
            throw new IllegalArgumentException("ConfigVar ID cannot be null or empty");
        }

        if(ObjectUtils.isEmpty(configVar.getValue())){
            throw new IllegalArgumentException("ConfigVar Value cannot be null or empty");
        }
    }

}
