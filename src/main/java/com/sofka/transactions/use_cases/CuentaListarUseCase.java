package com.sofka.transactions.use_cases;

import com.sofka.transactions.drivenAdapters.repositorios.I_RepositorioCuentaMongo;
import com.sofka.transactions.models.DTO.M_Cuenta_DTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Service
@Validated
@AllArgsConstructor
public class CuentaListarUseCase implements Supplier<Flux<M_Cuenta_DTO>> {
    private I_RepositorioCuentaMongo repositorioCuenta;
    private ModelMapper modelMapper;

    @Override
    public Flux<M_Cuenta_DTO> get() {
        return repositorioCuenta.findAll()
            .map(cuentaModel -> modelMapper.map(cuentaModel, M_Cuenta_DTO.class));
    }
}
