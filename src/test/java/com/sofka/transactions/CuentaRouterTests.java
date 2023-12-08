package com.sofka.transactions;

import com.sofka.transactions.handlers.CuentaHandler;
import com.sofka.transactions.models.DTO.M_Cuenta_DTO;
import com.sofka.transactions.routes.CuentaRouter;
import com.sofka.transactions.use_cases.CuentaCrearUseCase;
import com.sofka.transactions.use_cases.CuentaListarUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
//@WebFluxTest(controllers = CuentaHandler.class)
//@Import(CuentaRouter.class)
public class CuentaRouterTests {
//    @Autowired
    private WebTestClient webTestClient;
    @Mock
    private CuentaListarUseCase cuentaListarUseCase;
    @Mock
    private CuentaCrearUseCase cuentaCrearUseCase;
    private CuentaHandler cuentaHandler;
    private CuentaRouter cuentaRouter;

    @BeforeEach
    public void setup() {
        cuentaHandler = new CuentaHandler(cuentaListarUseCase, cuentaCrearUseCase);
        cuentaRouter = new CuentaRouter(cuentaHandler);
        webTestClient = WebTestClient.bindToRouterFunction(cuentaRouter.cuentaRoutes())
            .build();
    }

    @Test
    void listarCuentasTest() {
        M_Cuenta_DTO cuenta = new M_Cuenta_DTO();
        when(cuentaListarUseCase.get()).thenReturn(Flux.just(cuenta));

        webTestClient.get()
            .uri("/Cuentas/listar_cuentas")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(M_Cuenta_DTO.class)
            .isEqualTo(Arrays.asList(cuenta));
    }

    @Test
    void crearCuentaTest() {
        M_Cuenta_DTO cuenta = new M_Cuenta_DTO();
        when(cuentaCrearUseCase.apply(cuenta)).thenReturn(Mono.just(cuenta));

        webTestClient.post()
            .uri("/Cuentas/Crear")
            .bodyValue(cuenta)
            .exchange()
            .expectStatus().isOk()
            .expectBody(M_Cuenta_DTO.class)
            .isEqualTo(cuenta);
    }
}
