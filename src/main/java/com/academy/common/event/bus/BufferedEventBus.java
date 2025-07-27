package com.academy.common.event.bus;

import com.academy.common.constant.CommonConstant;
import com.academy.common.domain.Event;
import com.academy.common.processor.chain.ProcessorChain;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class BufferedEventBus {

    private static final Logger logger = LoggerFactory.getLogger(BufferedEventBus.class);

    @Autowired
    private ProcessorChain<Event> processorChain;

    private static final BlockingQueue<Event> QUEUE = new LinkedBlockingQueue<>();
    private static final ExecutorService WORKER_POOL = Executors.newSingleThreadExecutor();

    public BufferedEventBus() {
        WORKER_POOL.submit(this::dispatchLoop);
    }

    public void publish(String type, Object data, String... headers){
        Map<String, String> eventHeaders = new HashMap<>();
        eventHeaders.put(CommonConstant.X_TRACKING_ID, MDC.get(CommonConstant.X_TRACKING_ID));
        if(ArrayUtils.isNotEmpty(headers)){
            for(int index = 0; index < headers.length; index += 2) {
                if (index + 1 < headers.length) {
                    eventHeaders.put(headers[index], headers[index + 1]);
                } else {
                    logger.warn("Header key without value: {}", headers[index]);
                }
            }
        }

        Event event = Event.builder()
                .type(type)
                .data(data)
                .headers(eventHeaders)
                .build();

        boolean isPushed = QUEUE.offer(event);
        logger.debug("is event {} pushed to queue {}", event, isPushed);
    }

    private void dispatchLoop() {
        while (true) {
            try {
                if(!QUEUE.isEmpty()){
                    Event event = QUEUE.take();
                    if (ObjectUtils.isNotEmpty(event)) {
                        String trackingId = event.getHeaders().get(CommonConstant.X_TRACKING_ID);
                        if(StringUtils.isBlank(trackingId)) {
                            trackingId = UUID.randomUUID().toString();
                        }
                        MDC.put(CommonConstant.X_TRACKING_ID, trackingId);
                        logger.info("Processing event: {}", event.getType());
                        processorChain.process(event);
                    }
                }

            } catch (InterruptedException e) {
                logger.error("Event dispatch loop interrupted", e);
            } catch (Exception ex) {
                logger.error("Error processing event", ex);
            }
        }
    }
}
