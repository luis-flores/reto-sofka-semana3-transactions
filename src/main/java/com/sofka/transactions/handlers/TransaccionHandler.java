package com.sofka.transactions.handlers;

import com.sofka.transactions.models.DTO.M_Transaccion_DTO;
import com.sofka.transactions.models.tipos_transaccion.DepositoCajero;
import com.sofka.transactions.models.tipos_transaccion.DepositoOtraCuenta;
import com.sofka.transactions.models.tipos_transaccion.DepositoSucursal;
import com.sofka.transactions.use_cases.TransaccionListarUseCase;
import com.sofka.transactions.use_cases.TransaccionProcesarUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class TransaccionHandler {
    private TransaccionListarUseCase transaccionListarUseCase;
    private TransaccionProcesarUseCase transaccionProcesarUseCase;
    private DepositoCajero depositoCajero;
    private DepositoSucursal depositoSucursal;
    private DepositoOtraCuenta depositoOtraCuenta;

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok()
            .body(transaccionListarUseCase.get(), M_Transaccion_DTO.class);
    }
    public Mono<ServerResponse> procesarDepositoCajero(ServerRequest request) {
        String idCuenta = request.pathVariable("id_Cuenta");
        BigDecimal monto = new BigDecimal(request.pathVariable("monto"));

        return ServerResponse.ok()
            .body(transaccionProcesarUseCase.apply(idCuenta, depositoCajero, monto), M_Transaccion_DTO.class);
    }
    public Mono<ServerResponse> procesarDepositoSucursal(ServerRequest request) {
        String idCuenta = request.pathVariable("id_Cuenta");
        BigDecimal monto = new BigDecimal(request.pathVariable("monto"));

        return ServerResponse.ok()
            .body(transaccionProcesarUseCase.apply(idCuenta, depositoSucursal, monto), M_Transaccion_DTO.class);
    }
    public Mono<ServerResponse> procesarDepositoCuenta(ServerRequest request) {
        String idCuenta = request.pathVariable("id_Cuenta");
        BigDecimal monto = new BigDecimal(request.pathVariable("monto"));

        return ServerResponse.ok()
            .body(transaccionProcesarUseCase.apply(idCuenta, depositoOtraCuenta, monto), M_Transaccion_DTO.class);
    }
}
