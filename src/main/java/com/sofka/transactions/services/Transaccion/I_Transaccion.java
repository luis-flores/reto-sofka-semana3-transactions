package com.sofka.transactions.services.Transaccion;

import com.sofka.transactions.models.DTO.M_Transaccion_DTO;
import com.sofka.transactions.models.Enum_Tipos_Deposito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface I_Transaccion
{
    Mono<M_Transaccion_DTO> Procesar_Deposito(String id_Cuenta, Enum_Tipos_Deposito tipo, BigDecimal monto);

    Flux<M_Transaccion_DTO> findAll();

    Mono<Void> borrar(M_Transaccion_DTO transaccion);
}
