package com.sofka.transactions.use_cases;

import com.sofka.transactions.drivenAdapters.bus.RabbitMqPublisher;
import com.sofka.transactions.drivenAdapters.repositorios.I_RepositorioCuentaMongo;
import com.sofka.transactions.models.DTO.M_Cuenta_DTO;
import com.sofka.transactions.models.Mongo.M_CuentaMongo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CuentaCrearUseCaseTests {

    private CuentaCrearUseCase cuentaCrearUseCase;
    @Mock
    private I_RepositorioCuentaMongo repositorioCuenta;
    @Mock
    private RabbitMqPublisher eventBus;
    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        cuentaCrearUseCase = new CuentaCrearUseCase(repositorioCuenta, modelMapper, eventBus);
    }

    @Test
    public void crearCuentaTest() {
        M_CuentaMongo cuenta = new M_CuentaMongo();
        M_Cuenta_DTO cuentaDTO = modelMapper.map(cuenta, M_Cuenta_DTO.class);

        when(repositorioCuenta.save(cuenta)).thenReturn(Mono.just(cuenta));
        doNothing().when(eventBus).publishMessage(cuenta);

        StepVerifier.create(cuentaCrearUseCase.apply(cuentaDTO))
            .expectNext(cuentaDTO)
            .verifyComplete();
    }
}
