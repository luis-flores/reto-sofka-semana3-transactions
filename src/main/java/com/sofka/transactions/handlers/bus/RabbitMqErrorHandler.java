package com.sofka.transactions.handlers.bus;

import com.google.gson.Gson;
import com.sofka.transactions.RabbitConfig;
import com.sofka.transactions.models.DTO.M_Cuenta_DTO;
import com.sofka.transactions.models.DTO.M_Transaccion_DTO;
import com.sofka.transactions.use_cases.CuentaActualizarSaldoUseCase;
import com.sofka.transactions.use_cases.TransaccionBorrarUseCase;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.rabbitmq.Receiver;

@Component
public class RabbitMqErrorHandler implements CommandLineRunner {

    @Autowired
    private Receiver receiver;

    @Autowired
    private Gson gson;

    @Autowired
    CuentaActualizarSaldoUseCase cuentaActualizarSaldoUseCase;

    @Autowired
    TransaccionBorrarUseCase transaccionBorrarUseCase;

    @Autowired
    ModelMapper mapper;

    @Override
    public void run(String... args) throws Exception {
        receiver.consumeAutoAck(RabbitConfig.ERROR_QUEUE_NAME)
            .map(message -> {
                M_Transaccion_DTO transaccion = gson
                    .fromJson(new String(message.getBody()),
                        M_Transaccion_DTO.class);

                M_Cuenta_DTO cuenta = transaccion.getCuenta();
                cuenta.setSaldo_Global(transaccion.getSaldo_inicial());
                cuentaActualizarSaldoUseCase.apply(
                    mapper.map(cuenta, M_Cuenta_DTO.class)
                ).subscribe();

                transaccionBorrarUseCase.apply(
                    mapper.map(transaccion, M_Transaccion_DTO.class)
                ).subscribe();

                System.out.println("La transaccion a reversar fue:  " + transaccion);
                return transaccion;
            }).subscribe();
    }
}
