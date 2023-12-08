package com.sofka.transactions.use_cases;

import com.sofka.transactions.drivenAdapters.bus.RabbitMqPublisher;
import com.sofka.transactions.drivenAdapters.repositorios.I_RepositorioCuentaMongo;
import com.sofka.transactions.models.DTO.M_Cuenta_DTO;
import com.sofka.transactions.models.Mongo.M_CuentaMongo;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
@Validated
@AllArgsConstructor
public class CuentaCrearUseCase implements Function<M_Cuenta_DTO, Mono<M_Cuenta_DTO>> {
    private I_RepositorioCuentaMongo repositorioCuenta;
    private RabbitMqPublisher eventBus;
    private ModelMapper modelMapper;

    @Override
    public Mono<M_Cuenta_DTO> apply(M_Cuenta_DTO pCuentaDTO) {
        eventBus.publishLog("CuentaCrearUseCase: Inicio de ejecución");

        M_CuentaMongo cuenta = modelMapper.map(pCuentaDTO, M_CuentaMongo.class);
        eventBus.publishLog("CuentaCrearUseCase: Mapeo de cuenta: " + cuenta);

        eventBus.publishMessage(cuenta);

        return repositorioCuenta.save(cuenta)
            .doOnSuccess(cuentaGuardada -> eventBus.publishLog("CuentaCrearUseCase Cuenta guardada: ", cuentaGuardada))
            .map(cuentaModel-> modelMapper.map(cuentaModel, M_Cuenta_DTO.class))
            .doOnSuccess(cuentaMap -> eventBus.publishLog("CuentaCrearUseCase: Fin de ejecución"))
            .doOnError(error -> eventBus.publishLog("Cuenta"));
    }
}
