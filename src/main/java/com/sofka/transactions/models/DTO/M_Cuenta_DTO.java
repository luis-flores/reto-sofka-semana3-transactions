package com.sofka.transactions.models.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class M_Cuenta_DTO
{
    //@NotNull(message = "[CUENTA] [id] Campo Requerido: Id.")
    private String id;

    @Valid
    @NotNull(message = "[CUENTA] [Cliente] Campo Requerido: La Cuenta debe poseer información del Cliente.")
    private M_Cliente_DTO cliente;

    @DecimalMin(value = "0.00", inclusive = true, message = "[CUENTA] [saldo_Global] El Saldo Inicial deber ser mayor o igual a 0.00")
    @DecimalMax(value = "1000000.00", inclusive = true, message = "[CUENTA] [saldo_Global] El Saldo Inicial deber ser menor o igual a 1000000.00")
    //@Digits(integer = 7, fraction = 2, message = "[CUENTA] [saldo_Global] El Formato del Saldo debe ser 7 digitos enteros y 2 decimales")
    private BigDecimal saldo_Global;

    @Override
    public String toString() {
        return "M_Cuenta_DTO{" +
                "id='" + id + '\'' +
                ", cliente=" + cliente +
                ", saldo_Global=" + saldo_Global +
                '}';
    }
}
