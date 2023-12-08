package com.sofka.transactions.use_cases;

import com.sofka.transactions.drivenAdapters.bus.RabbitMqPublisher;
import com.sofka.transactions.drivenAdapters.repositorios.I_RepositorioCuentaMongo;
import com.sofka.transactions.drivenAdapters.repositorios.I_Repositorio_TransaccionMongo;
import com.sofka.transactions.models.DTO.M_Transaccion_DTO;
import com.sofka.transactions.models.Mongo.M_TransaccionMongo;
import com.sofka.transactions.models.ErrorGuardado;
import com.sofka.transactions.models.tipos_transaccion.ITipoTransaccion;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@Validated
@AllArgsConstructor
public class TransaccionProcesarUseCase implements ITransaccionProceso {
    private I_Repositorio_TransaccionMongo repositorioTransaccion;
    private I_RepositorioCuentaMongo repositorioCuenta;
    private RabbitMqPublisher eventBus;
    private ModelMapper modelMapper;

    @Override
    public Mono<M_Transaccion_DTO> apply(String idCuenta, ITipoTransaccion tipo, BigDecimal monto) {
        return repositorioCuenta.findById(idCuenta)
            .flatMap(cuenta -> {
                BigDecimal costo = tipo.getCosto();
                BigDecimal bdSaldoActual = cuenta.getSaldo_Global();
                BigDecimal bdSaldoNuevo = cuenta.getSaldo_Global().add(monto.subtract(costo));
                cuenta.setSaldo_Global(bdSaldoNuevo);
                M_TransaccionMongo transaccion = new M_TransaccionMongo(
                    cuenta,
                    monto,
                    bdSaldoActual,
                    bdSaldoNuevo,
                    costo,
                    tipo.toString()
                );
                return repositorioCuenta.save(cuenta)
                    .flatMap(cuentaCreada ->
                        Mono.error(new ErrorGuardado(
                            "Error de prueba",
                            transaccion
                        )))
                    .flatMap(c -> {
                        System.out.println("Transaccion guardada: " + transaccion.getId());
                        return repositorioTransaccion.save(transaccion);
                    })
                    .onErrorResume(error -> {
                        System.out.println("El error fue: " + error.getMessage());

                        if (error instanceof ErrorGuardado) {
                            ErrorGuardado errorGuardado = (ErrorGuardado) error;
                            eventBus.publishError(errorGuardado.getTransaccion());
                        }

                        return Mono.empty();
                    })
                    .map(transactionModel -> {
                        System.out.println("Transaccion guardada: " + transactionModel.getId());

                        return modelMapper.map(transactionModel, M_Transaccion_DTO.class);
                    });
            });
    }
}
