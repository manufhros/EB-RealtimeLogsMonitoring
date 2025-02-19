package com.example.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PaymentRoute extends RouteBuilder {
    private static final Logger logger = LogManager.getLogger(PaymentRoute.class);

    @Override
    public void configure() {
        from("timer:payment?period=60000") // Generates a payment event every 60 seconds
            .routeId("payment-route")
            .process(exchange -> {
                String paymentId = "TXN" + System.currentTimeMillis();
                exchange.getIn().setHeader("paymentId", paymentId);
                exchange.getIn().setBody("Processing payment with ID: " + paymentId);
                logger.info("Processing payment with ID: {}", paymentId);
            })
            .to("log:com.example.routes.PaymentRoute?level=INFO");
    }
}
