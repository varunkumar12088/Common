package com.academy.common.processor;

public interface Processor<T> {


    void process(T event);


    boolean canProcess(T event);
}
