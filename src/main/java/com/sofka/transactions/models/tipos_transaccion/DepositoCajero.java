package com.sofka.transactions.models.tipos_transaccion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DepositoCajero implements ITipoTransaccion {

    @Value("${EPA.Deposito.Cajero}")
    private String costo;

    @Override
    public BigDecimal getCosto() {
        return new BigDecimal(costo);
    }

    @Override
    public void setCosto(BigDecimal costo) {
        this.costo = costo.toString();
    }

    @Override
    public String toString() {
        return "DepositoCajero{" +
            "costo='" + costo + '\'' +
            '}';
    }
}