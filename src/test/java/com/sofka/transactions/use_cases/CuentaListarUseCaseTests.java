package com.sofka.transactions.use_cases;

import com.sofka.transactions.drivenAdapters.repositorios.I_RepositorioCuentaMongo;
import com.sofka.transactions.models.DTO.M_Cuenta_DTO;
import com.sofka.transactions.models.Mongo.M_CuentaMongo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@SpringBootTest
public class CuentaListarUseCaseTests {

    private CuentaListarUseCase cuentaListarUseCase;
    @Mock
    private I_RepositorioCuentaMongo repositorioCuenta;
    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        cuentaListarUseCase = new CuentaListarUseCase(repositorioCuenta, modelMapper);
    }

    @Test
    public void listarCuentasTest() {
        M_CuentaMongo cuenta = new M_CuentaMongo();
        when(repositorioCuenta.findAll()).thenReturn(Flux.just(cuenta));

        StepVerifier.create(cuentaListarUseCase.get())
            .expectNext(modelMapper.map(cuenta, M_Cuenta_DTO.class))
            .verifyComplete();
    }
}
