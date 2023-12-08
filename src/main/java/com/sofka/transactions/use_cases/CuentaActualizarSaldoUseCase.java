package com.sofka.transactions.use_cases;

import com.sofka.transactions.drivenAdapters.repositorios.I_RepositorioCuentaMongo;
import com.sofka.transactions.models.DTO.M_Cuenta_DTO;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
@Validated
@AllArgsConstructor
public class CuentaActualizarSaldoUseCase implements Function<M_Cuenta_DTO, Mono<M_Cuenta_DTO>> {
    private I_RepositorioCuentaMongo repositorioCuenta;
    private ModelMapper modelMapper;

    @Override
    public Mono<M_Cuenta_DTO> apply(M_Cuenta_DTO p_Cuenta) {
        String id = p_Cuenta.getId();
        return repositorioCuenta.findById(id).flatMap(cuentaModel -> {
            cuentaModel.setSaldo_Global(p_Cuenta.getSaldo_Global());
            return repositorioCuenta.save(cuentaModel);
        }).map(cuentaModel -> modelMapper.map(cuentaModel, M_Cuenta_DTO.class));
    }
}
