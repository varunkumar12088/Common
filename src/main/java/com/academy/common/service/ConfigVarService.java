package com.academy.common.service;

import java.io.Serializable;

public interface ConfigVarService<T, ID extends Serializable> {

    void save(T t);
    <T> T getConfigVar(ID id);
    <T> T getConfigVarValue(ID id, Class<T> valueType);
    <T> T getConfigVarValue(ID id, Class<T> valueType, T defaultValue);


}
