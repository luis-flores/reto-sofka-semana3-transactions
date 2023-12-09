package com.sofka.transactions.use_cases;

import com.sofka.transactions.drivenAdapters.bus.RabbitMqPublisher;
import com.sofka.transactions.drivenAdapters.repositorios.I_RepositorioCuentaMongo;
import com.sofka.transactions.drivenAdapters.repositorios.I_Repositorio_TransaccionMongo;
import com.sofka.transactions.models.DTO.M_Transaccion_DTO;
import com.sofka.transactions.models.Mongo.M_CuentaMongo;
import com.sofka.transactions.models.Mongo.M_TransaccionMongo;
import com.sofka.transactions.models.tipos_transaccion.DepositoCajero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransaccionProcesarUseCaseTests {
    private TransaccionProcesarUseCase transaccionProcesarUseCase;

    @Mock
    private I_Repositorio_TransaccionMongo repositorioTransaccion;
    @Mock
    private I_RepositorioCuentaMongo repositorioCuenta;

    @Mock
    private RabbitMqPublisher eventBus;
    private ModelMapper modelMapper;
    private DepositoCajero depositoCajero;

    @BeforeEach
    public void setup() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
            .setFieldMatchingEnabled(true)
            .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        depositoCajero = new DepositoCajero();
        depositoCajero.setCosto(new BigDecimal("2.00"));

        transaccionProcesarUseCase = new TransaccionProcesarUseCase(repositorioTransaccion, repositorioCuenta, eventBus, modelMapper);
    }

    @Test
    public void procesarDepositoCajeroTest() {
        M_CuentaMongo cuenta = new M_CuentaMongo();
        String idCuenta = "<id-cuenta>";
        cuenta.setId(idCuenta);
        cuenta.setSaldo_Global(new BigDecimal(0));
        BigDecimal monto = new BigDecimal(1);
        M_TransaccionMongo transaccion = new M_TransaccionMongo();
        M_Transaccion_DTO transaccionDTO = modelMapper.map(transaccion, M_Transaccion_DTO.class);

        when(repositorioCuenta.findById(idCuenta)).thenReturn(Mono.just(cuenta));
        when(repositorioCuenta.save(cuenta)).thenReturn(Mono.just(cuenta));
        when(repositorioTransaccion.save(any(M_TransaccionMongo.class))).thenReturn(Mono.just(transaccion));
//        doNothing().when(eventBus).publishError(any(String.class));

        StepVerifier.create(transaccionProcesarUseCase.apply(idCuenta, depositoCajero, monto))
            .expectNext(transaccionDTO)
            .verifyComplete();
    }
}
