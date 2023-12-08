package com.sofka.transactions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationPrinter {

    public ConfigurationPrinter(
        @Value("${server.port}") String serverPort,
        @Value("${EPA.Deposito.Sucursal}") String depositoSucursal,
        @Value("${EPA.Deposito.Cajero}") String depositoCajero,
        @Value("${EPA.Deposito.OtraCuenta}") String depositoCuenta,
        @Value("${EPA.Compra.Fisica}") String compraFisica,
        @Value("${EPA.Compra.Web}") String compraWeb,
        @Value("${EPA.Retiro.Cajero}") String retiroCajero,
        @Value("${mongo.uri}") String mongoUri,
        @Value("${rabbit.uri}") String rabbitUri
    ) {
        System.out.println("*** CONFIGURACION ***");
        System.out.println("Server Port: " + serverPort);
        System.out.println("Deposito Sucursal: " + depositoSucursal);
        System.out.println("Deposito Cajero: " + depositoCajero);
        System.out.println("Deposito Cuenta: " + depositoCuenta);
        System.out.println("Compra Fisica: " + compraFisica);
        System.out.println("Compra Web: " + compraWeb);
        System.out.println("Retiro Cajero: " + retiroCajero);
        System.out.println("Mongo URI: " + mongoUri);
        System.out.println("RabbitMQ URI: " + rabbitUri);
        System.out.println("*** CONFIGURACION ***");
    }
}
