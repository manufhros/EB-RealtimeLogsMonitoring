package com.example;

import com.example.routes.*;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Main application class to start Apache Camel and generate logs.
 */
public class App {
    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        CamelContext camelContext = new DefaultCamelContext();

        try {
            // Add all defined Camel routes
            camelContext.addRoutes(new PaymentRoute());       // Generates logs automatically every 60 sec
            camelContext.addRoutes(new TransactionRoute());   // Requires events
            camelContext.addRoutes(new NotificationRoute());  // Requires events
            camelContext.addRoutes(new AuditRoute());         // Requires events
            camelContext.addRoutes(new ErrorHandlingRoute()); // Logs only if an error occurs

            logger.info("Starting Apache Camel application...");
            camelContext.start();

            // Create a producer to manually send events to routes that don't have a timer
            ProducerTemplate producer = camelContext.createProducerTemplate();
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

            // Simulate incoming events every 5 seconds for routes that don't have a timer
            scheduler.scheduleAtFixedRate(() -> {
                producer.sendBody("direct:logAudit", "Simulated audit event");
                producer.sendBodyAndHeader("direct:sendNotification", "Test notification message", "user", "user1");
                producer.sendBodyAndHeader("direct:processTransaction", "100.00", "paymentId", "TXN" + System.currentTimeMillis());
            }, 0, 5, TimeUnit.SECONDS);

            // Keep the application running indefinitely
            Thread.sleep(Long.MAX_VALUE);

        } catch (Exception e) {
            logger.error("Error while starting Camel: {}", e.getMessage(), e);
        } finally {
            try {
                logger.info("Stopping Apache Camel application...");
                camelContext.stop();
            } catch (Exception e) {
                logger.error("Error while stopping Camel: {}", e.getMessage(), e);
            }
        }
    }
}
