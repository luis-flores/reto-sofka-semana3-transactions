package com.sofka.transactions.models.tipos_transaccion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DepositoOtraCuenta implements ITipoTransaccion {

    @Value("${EPA.Deposito.OtraCuenta}")
    private String costo;

    @Override
    public BigDecimal getCosto() {
        return new BigDecimal(costo);
    }
}
