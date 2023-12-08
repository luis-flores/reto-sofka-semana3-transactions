package com.sofka.transactions.use_cases;

import com.sofka.transactions.drivenAdapters.bus.RabbitMqPublisher;
import com.sofka.transactions.drivenAdapters.repositorios.I_Repositorio_TransaccionMongo;
import com.sofka.transactions.models.DTO.M_Transaccion_DTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Service
@Validated
@AllArgsConstructor
public class TransaccionListarUseCase implements Supplier<Flux<M_Transaccion_DTO>> {
    private I_Repositorio_TransaccionMongo repositorioTransaccion;
    private RabbitMqPublisher eventBus;
    private ModelMapper modelMapper;

    @Override
    public Flux<M_Transaccion_DTO> get()
    {
        eventBus.publishLog("TransaccionListarUseCase: Inicio de ejecución");

        return repositorioTransaccion.findAll()
            .doOnComplete(() -> eventBus.publishLog("TransaccionListarUseCase: Consulta completada"))
            .map(transaccion -> modelMapper.map(transaccion, M_Transaccion_DTO.class))
            .doOnComplete(() -> eventBus.publishLog("TransaccionListarUseCase: Fin de ejecución"))
            .doOnError(error -> eventBus.publishLog("TransaccionListarUseCase: Error: ", error));
    }
}
