package com.sofka.transactions.use_cases;

import com.sofka.transactions.drivenAdapters.bus.RabbitMqPublisher;
import com.sofka.transactions.drivenAdapters.repositorios.I_RepositorioCuentaMongo;
import com.sofka.transactions.models.DTO.M_Cuenta_DTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Service
@Validated
@AllArgsConstructor
public class CuentaListarUseCase implements Supplier<Flux<M_Cuenta_DTO>> {
    private I_RepositorioCuentaMongo repositorioCuenta;
    private RabbitMqPublisher eventBus;
    private ModelMapper modelMapper;

    @Override
    public Flux<M_Cuenta_DTO> get() {
        eventBus.publishLog("CuentaListarUseCase: Inicio de ejecución");

        return repositorioCuenta.findAll()
            .doOnComplete(() -> eventBus.publishLog("CuentaListarUseCase: Consulta completada"))
            .map(cuentaModel -> modelMapper.map(cuentaModel, M_Cuenta_DTO.class))
            .doOnComplete(() -> eventBus.publishLog("CuentaListarUseCase: Fin de ejecución"))
            .doOnError(error -> eventBus.publishLog("CuentaListarUseCase Error: ", error));
    }
}
