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
public class TransaccionRouter {
    @Autowired
    private TransaccionHandler transaccion;
    @Bean
    public RouterFunction<ServerResponse> transaccionRoutes() {
        return RouterFunctions.route()
            .GET("/Transacciones/listar_transacciones", transaccion::findAll)
            .POST("/Transacciones/Crear/Deposito/Cajero/{id_Cuenta}/{monto}", transaccion::procesarDepositoCajero)
            .POST("/Transacciones/Crear/Deposito/Sucursal/{id_Cuenta}/{monto}", transaccion::procesarDepositoSucursal)
            .POST("/Transacciones/Crear/Deposito/OtraCuenta/{id_Cuenta}/{monto}", transaccion::procesarDepositoCuenta)

            .build();
    }
}
