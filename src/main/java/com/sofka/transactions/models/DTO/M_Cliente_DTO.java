package com.sofka.transactions.models.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class M_Cliente_DTO
{
    @NotNull(message = "[CLIENTE] [id] Campo Requerido: Id.")
    private String id;

    @NotNull(message = "[CLIENTE] [nombre] Campo Requerido: Id.")
    private String nombre;
}
