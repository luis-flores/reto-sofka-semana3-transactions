package com.sofka.transactions.use_cases;

import com.sofka.transactions.models.DTO.M_Transaccion_DTO;
import com.sofka.transactions.models.tipos_transaccion.ITipoTransaccion;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@FunctionalInterface
public interface ITransaccionProceso {
    Mono<M_Transaccion_DTO> apply(String idCuenta, ITipoTransaccion tipo, BigDecimal monto);
}
