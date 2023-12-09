package com.sofka.transactions.use_cases;

import com.sofka.transactions.drivenAdapters.bus.RabbitMqPublisher;
import com.sofka.transactions.drivenAdapters.repositorios.I_Repositorio_TransaccionMongo;
import com.sofka.transactions.models.DTO.M_Transaccion_DTO;
import com.sofka.transactions.models.Mongo.M_TransaccionMongo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TransaccionListarUseCaseTests {

    private TransaccionListarUseCase transaccionListarUseCase;
    @Mock
    private I_Repositorio_TransaccionMongo repositorioTransaccion;
    @Mock
    private RabbitMqPublisher eventBus;
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
            .setFieldMatchingEnabled(true)
            .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        transaccionListarUseCase = new TransaccionListarUseCase(repositorioTransaccion, eventBus, modelMapper);
    }

    @Test
    public void listarTransacciones() {
        M_TransaccionMongo transaccion = new M_TransaccionMongo();
        when(repositorioTransaccion.findAll()).thenReturn(Flux.just(transaccion));

        StepVerifier.create(transaccionListarUseCase.get())
            .expectNext(modelMapper.map(transaccion, M_Transaccion_DTO.class))
            .verifyComplete();
    }

}
