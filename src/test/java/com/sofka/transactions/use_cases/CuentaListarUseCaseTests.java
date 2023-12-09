package com.sofka.transactions.use_cases;

import com.sofka.transactions.drivenAdapters.bus.RabbitMqPublisher;
import com.sofka.transactions.drivenAdapters.repositorios.I_RepositorioCuentaMongo;
import com.sofka.transactions.models.DTO.M_Cuenta_DTO;
import com.sofka.transactions.models.Mongo.M_CuentaMongo;
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
public class CuentaListarUseCaseTests {

    private CuentaListarUseCase cuentaListarUseCase;
    @Mock
    private I_RepositorioCuentaMongo repositorioCuenta;
    @Mock
    private RabbitMqPublisher eventBus;
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
            .setFieldMatchingEnabled(true)
            .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        cuentaListarUseCase = new CuentaListarUseCase(repositorioCuenta, eventBus, modelMapper);
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
