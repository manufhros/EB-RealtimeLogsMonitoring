package com.example.routes;

import org.apache.camel.builder.RouteBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RutaPago extends RouteBuilder {
    private static final Logger logger = LogManager.getLogger(RutaPago.class);

    @Override
    public void configure() {
        from("direct:procesarPago") // Inicia la ruta cuando recibe un mensaje en "direct:procesarPago"
            .routeId("ruta-procesar-pago")
            .process(exchange -> {
                String paymentId = exchange.getIn().getHeader("paymentId", String.class);
                String amount = exchange.getIn().getHeader("amount", String.class);

                logger.info("Procesando pago con ID: {} por un monto de {}", paymentId, amount);

                exchange.getIn().setBody("Pago procesado exitosamente: ID=" + paymentId + ", Monto=" + amount);
            })
            .to("log:com.ejemplo.rutas.RutaPago?level=INFO");
    }
}
