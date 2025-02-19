package com.example.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TransactionRoute extends RouteBuilder {
    private static final Logger logger = LogManager.getLogger(TransactionRoute.class);

    @Override
    public void configure() {
        from("direct:processTransaction")
            .routeId("transaction-route")
            .process(exchange -> {
                String paymentId = exchange.getIn().getHeader("paymentId", String.class);
                String amount = exchange.getIn().getHeader("amount", String.class);

                logger.info("Processing transaction ID: {} for amount: {}", paymentId, amount);

                exchange.getIn().setBody("Transaction successful: ID=" + paymentId + ", Amount=" + amount);
            })
            .to("log:com.example.routes.TransactionRoute?level=INFO");
    }
}
