package com.sofka.transactions.use_cases;

import com.sofka.transactions.drivenAdapters.repositorios.I_Repositorio_TransaccionMongo;
import com.sofka.transactions.models.DTO.M_Transaccion_DTO;
import com.sofka.transactions.models.Mongo.M_TransaccionMongo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;


@SpringBootTest
public class TransaccionListarUseCaseTests {

    private TransaccionListarUseCase transaccionListarUseCase;
    @Mock
    private I_Repositorio_TransaccionMongo repositorioTransaccion;
    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        transaccionListarUseCase = new TransaccionListarUseCase(repositorioTransaccion, modelMapper);
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
