package com.example.routes;

import org.apache.camel.builder.RouteBuilder;  // Apache Camel class to define routes.
import org.apache.logging.log4j.LogManager;   // Log4j2 class for logging.
import org.apache.logging.log4j.Logger;      // Log4j2 Logger instance.

public class AuditRoute extends RouteBuilder {
    private static final Logger logger = LogManager.getLogger(AuditRoute.class);  // Creates a Logger instance for this class.

    @Override
    public void configure() {  // The configure() method defines the behavior of the Camel route.
        
        // The route starts when a message is sent to "direct:logAudit".
        from("direct:logAudit")
            .routeId("audit-route")  // Assigns a unique ID to the route for tracking.

            // Processes the incoming message.
            .process(exchange -> {
                String event = exchange.getIn().getBody(String.class);  // Extracts the message body as a String.
                logger.info("Audit event recorded: {}", event);  // Logs the audit event using Log4j2.
            })

            // Sends the log output to Apache Camel's internal logging system at INFO level.
            .to("log:com.example.routes.AuditRoute?level=INFO");
    }
}
