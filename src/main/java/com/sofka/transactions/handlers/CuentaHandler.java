package com.sofka.transactions.handlers;

import com.sofka.transactions.models.DTO.M_Cuenta_DTO;
import com.sofka.transactions.services.Cuenta.I_Cuenta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CuentaHandler {
    @Autowired
    private I_Cuenta cuentaService;
    public Mono<ServerResponse> findAll(ServerRequest request) {
        Flux<M_Cuenta_DTO> cuentas = cuentaService.findAll();
        return ServerResponse.ok()
            .contentType(MediaType.TEXT_EVENT_STREAM)
            .body(cuentas, M_Cuenta_DTO.class);
    }
    public Mono<ServerResponse> crear(ServerRequest request) {
        return request.bodyToMono(M_Cuenta_DTO.class)
            .flatMap(cuenta -> {
                Mono<M_Cuenta_DTO> cuentaCreada = cuentaService.crear_Cuenta(cuenta);
                return ServerResponse.ok()
                    .body(cuentaCreada, M_Cuenta_DTO.class);
            });
    }
}
