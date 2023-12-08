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
    private ModelMapper modelMapper;
    private RabbitMqPublisher eventBus;

    @Override
    public Mono<M_Cuenta_DTO> apply(M_Cuenta_DTO pCuentaDTO) {
        M_CuentaMongo cuenta = modelMapper.map(pCuentaDTO, M_CuentaMongo.class);

        eventBus.publishMessage(cuenta);

        return repositorioCuenta.save(cuenta)
            .map(cuentaModel-> modelMapper.map(cuentaModel, M_Cuenta_DTO.class));
    }
}
