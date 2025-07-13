package com.academy.common.processor.chain;

import com.academy.common.processor.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcessorChain<T> {

    // Logger
    private static final Logger logger = LoggerFactory.getLogger(ProcessorChain.class);
    @Autowired
    private List<Processor<T>> processors;

    public void process(T event) {
        if (event == null) {
            logger.warn("Received null event, skipping processing.");
            return;
        }
        logger.info("Processing event: {}", event);
        for (Processor<T> processor : processors) {
            if (processor.canProcess(event)) {
                logger.info("Processor {} can process event: {}", processor.getClass().getSimpleName(), event);
                processor.process(event);
                return;
            }
        }
    }
}
