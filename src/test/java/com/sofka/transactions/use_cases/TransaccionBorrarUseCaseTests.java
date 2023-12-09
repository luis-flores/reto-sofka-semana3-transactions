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
import org.springframework.data.domain.Example;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransaccionBorrarUseCaseTests {

    private TransaccionBorrarUseCase transaccionBorrarUseCase;
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

        transaccionBorrarUseCase = new TransaccionBorrarUseCase(repositorioTransaccion, eventBus, modelMapper);
    }

    @Test
    public void borrarTransaccionTest() {
        M_Transaccion_DTO transaccionDTO = new M_Transaccion_DTO();
        transaccionDTO.setId("<id-transaccion>");
        M_TransaccionMongo transaccion = modelMapper.map(transaccionDTO, M_TransaccionMongo.class);

        when(repositorioTransaccion.findOne(Example.of(transaccion))).thenReturn(Mono.just(transaccion));
        when(repositorioTransaccion.deleteById(transaccion.getId())).thenReturn(Mono.empty());

        StepVerifier.create(transaccionBorrarUseCase.apply(transaccionDTO))
            .verifyComplete();
    }
}
