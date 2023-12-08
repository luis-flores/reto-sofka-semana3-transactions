package com.sofka.transactions.use_cases;

import com.sofka.transactions.drivenAdapters.bus.RabbitMqPublisher;
import com.sofka.transactions.drivenAdapters.repositorios.I_RepositorioCuentaMongo;
import com.sofka.transactions.models.DTO.M_Cuenta_DTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
@Validated
@AllArgsConstructor
public class CuentaActualizarSaldoUseCase implements Function<M_Cuenta_DTO, Mono<M_Cuenta_DTO>> {
    private I_RepositorioCuentaMongo repositorioCuenta;
    private RabbitMqPublisher eventBus;
    private ModelMapper modelMapper;

    @Override
    public Mono<M_Cuenta_DTO> apply(M_Cuenta_DTO p_Cuenta) {
        eventBus.publishLog("CuentaActualizarSaldoUseCase: Inicio de ejecución");

        String id = p_Cuenta.getId();
        eventBus.publishLog("CuentaActualizarSaldoUseCase Id de la cuenta: " + id);

        return repositorioCuenta.findById(id)
            .doOnSuccess(cuentaEncontrada -> eventBus.publishLog("CuentaActualizarSaldoUseCase Cuenta encontrada: ", cuentaEncontrada))
            .flatMap(cuentaModel -> {
                cuentaModel.setSaldo_Global(p_Cuenta.getSaldo_Global());
                eventBus.publishLog("CuentaActualizarSaldoUseCase asignado saldo: ", cuentaModel);
                return repositorioCuenta.save(cuentaModel);
            })
            .doOnSuccess(cuentaGuardada -> eventBus.publishLog("CuentaActualizarSaldoUseCase Cuenta guardada: ", cuentaGuardada))
            .map(cuentaModel -> modelMapper.map(cuentaModel, M_Cuenta_DTO.class))
            .doOnSuccess(cuentaMap -> eventBus.publishLog("CuentaActualizarSaldoUseCase: Fin de ejecución"))
            .doOnError(error -> eventBus.publishLog("CuentaActualizarSaldoUseCase Error: ", error));
    }
}
