package com.sofka.transactions.models.tipos_transaccion;

import java.math.BigDecimal;

public interface ITipoTransaccion {
    public BigDecimal getCosto();
    public void setCosto(BigDecimal costo);
    public String toString();
}
