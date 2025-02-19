package com.example.routes;

import org.apache.camel.builder.RouteBuilder;  // Imports Apache Camel’s RouteBuilder to define a route.
import org.apache.logging.log4j.LogManager;   // Log4j2 for logging errors.
import org.apache.logging.log4j.Logger;      // Logger instance for logging messages.

/*
 * ErrorHandlingRoute does not actively "listen" for exceptions like an event listener. 
 * Instead, it is notified automatically by Apache Camel whenever an exception occurs in any route.
 * This is achieved using Camel’s built-in error handling mechanism.
 */

public class ErrorHandlingRoute extends RouteBuilder {
    private static final Logger logger = LogManager.getLogger(ErrorHandlingRoute.class);

    @Override
    public void configure() {

        // Handles any exception thrown by Camel routes.
        onException(Exception.class)
            .routeId("error-handling-route")

            // Processes the exception and logs it.
            .process(exchange -> {
                Exception exception = exchange.getProperty(Exception.class.getName(), Exception.class);  // Extracts the exception from the Camel exchange.
                logger.error("An error occurred: {}", exception.getMessage());  // Logs the error using Log4j2.
            })

            .to("log:com.example.routes.ErrorHandlingRoute?level=ERROR");
    }
}
