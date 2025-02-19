package com.example.routes;

import org.apache.camel.builder.RouteBuilder;  // Import Apache Camel's RouteBuilder to define the route.
import org.apache.logging.log4j.LogManager;   // Import Log4j2 for logging notifications.
import org.apache.logging.log4j.Logger;      // Logger instance to log messages.

public class NotificationRoute extends RouteBuilder {
    private static final Logger logger = LogManager.getLogger(NotificationRoute.class);

    @Override
    public void configure() {
    	
    	from("direct:sendNotification")  // The route is triggered when a message is sent to "direct:sendNotification".
        .routeId("notification-route")

        .process(exchange -> {
            String user = exchange.getIn().getHeader("user", String.class);  // Extracts the "user" header from the message.
            String message = exchange.getIn().getBody(String.class);  // Extracts the message body (notification content).
            
            logger.info("Sending notification to {}: {}", user, message);  // Logs the notification event, indicating who the recipient is.
        })

        .to("log:com.example.routes.NotificationRoute?level=INFO");
    }
}