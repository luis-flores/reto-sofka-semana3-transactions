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
import org.mockito.Mock;
import org.mockito.internal.matchers.Any;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TransaccionProcesarUseCaseTests {
    private TransaccionProcesarUseCase transaccionProcesarUseCase;

    @Mock
    private I_Repositorio_TransaccionMongo repositorioTransaccion;
    @Mock
    private I_RepositorioCuentaMongo repositorioCuenta;

    @Mock
    private RabbitMqPublisher eventBus;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DepositoCajero depositoCajero;

    @BeforeEach
    public void setup() {
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
        doNothing().when(eventBus).publishError(any(Object.class));

        StepVerifier.create(transaccionProcesarUseCase.apply(idCuenta, depositoCajero, monto))
//            .expectNext(transaccionDTO)
            .verifyComplete();
    }
}
