package com.sofka.transactions.routes;

import com.sofka.transactions.handlers.TransaccionHandler;
import com.sofka.transactions.models.DTO.M_Transaccion_DTO;
import com.sofka.transactions.models.tipos_transaccion.DepositoCajero;
import com.sofka.transactions.models.tipos_transaccion.DepositoOtraCuenta;
import com.sofka.transactions.models.tipos_transaccion.DepositoSucursal;
import com.sofka.transactions.routes.TransaccionRouter;
import com.sofka.transactions.use_cases.TransaccionListarUseCase;
import com.sofka.transactions.use_cases.TransaccionProcesarUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransaccionRouterTests {
    private WebTestClient webTestClient;

    @Mock
    private TransaccionListarUseCase transaccionListarUseCase;
    @Mock
    private TransaccionProcesarUseCase transaccionProcesarUseCase;

    @Autowired
    private DepositoCajero depositoCajero;
    @Autowired
    private DepositoSucursal depositoSucursal;
    @Autowired
    private DepositoOtraCuenta depositoOtraCuenta;

    private TransaccionHandler transaccionHandler;
    private TransaccionRouter transaccionRouter;

    @BeforeEach
    public void setup() {
        transaccionHandler = new TransaccionHandler(transaccionListarUseCase, transaccionProcesarUseCase, depositoCajero, depositoSucursal, depositoOtraCuenta);
        transaccionRouter = new TransaccionRouter(transaccionHandler);
        webTestClient = WebTestClient.bindToRouterFunction(transaccionRouter.transaccionRoutes())
            .build();
    }

    @Test
    void listarTransaccionesTest() {
        M_Transaccion_DTO transaccion = new M_Transaccion_DTO();
        when(transaccionListarUseCase.get()).thenReturn(Flux.just(transaccion));

        webTestClient.get()
            .uri("/Transacciones/listar_transacciones")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(M_Transaccion_DTO.class)
            .isEqualTo(Arrays.asList(transaccion));
    }

    @Test
    void depositoCajeroTest() {
        M_Transaccion_DTO transaccion = new M_Transaccion_DTO();
        String idCuenta = "<id-cuenta>";
        BigDecimal monto = new BigDecimal("1.00");
        when(transaccionProcesarUseCase.apply(idCuenta, depositoCajero, monto)).thenReturn(Mono.just(transaccion));

        webTestClient.post()
            .uri("/Transacciones/Crear/Deposito/Cajero/{id_Cuenta}/{monto}", idCuenta, monto)
            .exchange()
            .expectStatus().isOk()
            .expectBody(M_Transaccion_DTO.class)
            .isEqualTo(transaccion);
    }

    @Test
    void depositoSucursalTest() {
        M_Transaccion_DTO transaccion = new M_Transaccion_DTO();
        String idCuenta = "<id-cuenta>";
        BigDecimal monto = new BigDecimal("1.00");
        when(transaccionProcesarUseCase.apply(idCuenta, depositoSucursal, monto)).thenReturn(Mono.just(transaccion));

        webTestClient.post()
            .uri("/Transacciones/Crear/Deposito/Sucursal/{id_Cuenta}/{monto}", idCuenta, monto)
            .exchange()
            .expectStatus().isOk()
            .expectBody(M_Transaccion_DTO.class)
            .isEqualTo(transaccion);
    }

    @Test
    void depositoOtraCuentaTest() {
        M_Transaccion_DTO transaccion = new M_Transaccion_DTO();
        String idCuenta = "<id-cuenta>";
        BigDecimal monto = new BigDecimal("1.00");
        when(transaccionProcesarUseCase.apply(idCuenta, depositoOtraCuenta, monto)).thenReturn(Mono.just(transaccion));

        webTestClient.post()
            .uri("/Transacciones/Crear/Deposito/OtraCuenta/{id_Cuenta}/{monto}", idCuenta, monto)
            .exchange()
            .expectStatus().isOk()
            .expectBody(M_Transaccion_DTO.class)
            .isEqualTo(transaccion);
    }
}
