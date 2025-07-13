package com.academy.common.processor;

public interface Processor<T> {

    boolean canProcess(T event);

    void process(T event);



}
