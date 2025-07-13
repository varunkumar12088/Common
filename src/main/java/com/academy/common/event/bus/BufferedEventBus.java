package com.academy.common.event.bus;

import com.academy.common.constant.CommonConstant;
import com.academy.common.domain.Event;
import com.academy.common.processor.chain.ProcessorChain;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    private volatile boolean running = true;

    public BufferedEventBus() {
        WORKER_POOL.submit(this::dispatchLoop);
    }

    public void publish(Event event) {
        QUEUE.offer(event);
    }

    private void dispatchLoop() {
        while (running) {
            try {
                Event event = QUEUE.take();
                if (event != null) {
                    String trackingId = event.getHeaders().get(CommonConstant.X_TRACKING_ID);
                    if(StringUtils.isBlank(trackingId)) {
                        trackingId = UUID.randomUUID().toString();
                    }
                    MDC.put(CommonConstant.X_TRACKING_ID, trackingId);
                    logger.info("Processing event: {}", event.getType());
                    processorChain.process(event);
                }
            } catch (InterruptedException e) {
                logger.error("Event dispatch loop interrupted", e);
            } catch (Exception ex) {
                logger.error("Error processing event", ex);
            }
        }
    }
}
