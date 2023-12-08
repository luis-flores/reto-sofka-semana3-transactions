package com.sofka.transactions.use_cases;

import com.sofka.transactions.drivenAdapters.bus.RabbitMqPublisher;
import com.sofka.transactions.drivenAdapters.repositorios.I_Repositorio_TransaccionMongo;
import com.sofka.transactions.models.DTO.M_Transaccion_DTO;
import com.sofka.transactions.models.Mongo.M_CuentaMongo;
import com.sofka.transactions.models.Mongo.M_TransaccionMongo;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
@Validated
@AllArgsConstructor
public class TransaccionBorrarUseCase implements Function<M_Transaccion_DTO, Mono<Void>> {
    private I_Repositorio_TransaccionMongo repositorioTransaccion;
    private RabbitMqPublisher eventBus;
    private ModelMapper modelMapper;

    @Override
    public Mono<Void> apply(M_Transaccion_DTO transaccionDTO) {
        eventBus.publishLog("TransaccionBorrarUseCase: Inicio de ejecución");

        String id = transaccionDTO.getId();
        M_TransaccionMongo transaccion;
        if (id == null || id.isEmpty())
        {
            transaccion = new M_TransaccionMongo();
            transaccion.setSaldo_inicial(transaccionDTO.getSaldo_inicial());
            transaccion.setSaldo_final(transaccionDTO.getSaldo_final());

            transaccion.setCuenta(
                modelMapper.map(transaccionDTO.getCuenta(), M_CuentaMongo.class)
            );
        }
        else
        {
            transaccion = new M_TransaccionMongo();
            transaccion.setId(transaccionDTO.getId());
        }

        eventBus.publishLog("TransaccionBorrarUseCase Transaccion a reversar: ", transaccion);

        repositorioTransaccion.findOne(Example.of(transaccion))
            .doOnSuccess(transaccionFind -> eventBus.publishLog("TransaccionBorrarUseCase Transaccion encontrada: ", transaccionFind))
            .map(transaccionEncontrada -> repositorioTransaccion.deleteById(transaccionEncontrada.getId()))
            .doOnSuccess(transaccionMap -> eventBus.publishLog("TransaccionBorrarUseCase Transaccion eliminada: ", transaccionMap))
            .doOnError(error -> eventBus.publishLog("TransaccionBorrarUseCase Error: ", error))
            .subscribe();

        return Mono.empty();
    }
}
