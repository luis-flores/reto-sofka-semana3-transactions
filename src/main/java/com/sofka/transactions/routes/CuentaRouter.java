package com.sofka.transactions.routes;

import com.sofka.transactions.handlers.CuentaHandler;
import com.sofka.transactions.handlers.TransaccionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class CuentaRouter {
    @Autowired
    private CuentaHandler cuenta;
    @Bean
    public RouterFunction<ServerResponse> cuentaRoutes() {
        return RouterFunctions.route()
            .GET("/Cuentas/listar_cuentas", cuenta::findAll)
            .POST("/Cuentas/Crear", cuenta::crear)

            .build();
    }
}
