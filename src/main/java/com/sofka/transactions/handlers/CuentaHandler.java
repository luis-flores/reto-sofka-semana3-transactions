package com.sofka.transactions.handlers;

import com.sofka.transactions.models.DTO.M_Cuenta_DTO;
import com.sofka.transactions.use_cases.CuentaCrearUseCase;
import com.sofka.transactions.use_cases.CuentaListarUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class CuentaHandler {
    private CuentaListarUseCase cuentaListarUseCase;
    private CuentaCrearUseCase cuentaCrearUseCase;

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok()
            .contentType(MediaType.TEXT_EVENT_STREAM)
            .body(cuentaListarUseCase.get(), M_Cuenta_DTO.class);
    }

    public Mono<ServerResponse> crear(ServerRequest request) {
        return request.bodyToMono(M_Cuenta_DTO.class)
            .flatMap(cuenta -> ServerResponse.ok()
                .body(cuentaCrearUseCase.apply(cuenta), M_Cuenta_DTO.class)
            );
    }
}
