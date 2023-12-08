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
import org.springframework.data.domain.Example;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@SpringBootTest
public class TransaccionBorrarUseCaseTests {

    private TransaccionBorrarUseCase transaccionBorrarUseCase;
    @Mock
    private I_Repositorio_TransaccionMongo repositorioTransaccion;
    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        transaccionBorrarUseCase = new TransaccionBorrarUseCase(repositorioTransaccion, modelMapper);
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
