package com.taller.gestion_taller.domain.model;

import com.taller.gestion_taller.application.command.ModificarClienteCommand;
import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Cliente {

    private Long id;
    private String dni;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String direccion;
    @Builder.Default
    private boolean activo = true;
    @Builder.Default
    private LocalDate fechaCreacion = LocalDate.now();
    @Builder.Default
    private LocalDate fechaModificacion = LocalDate.now();
    private List<Vehiculo> vehiculos;

    public Cliente actualizarDatos(ModificarClienteCommand command) {
        return this.toBuilder()
                .nombre(command.nombre())
                .apellido(command.apellido())
                .telefono(command.telefono())
                .email(command.email())
                .direccion(command.direccion())
                .fechaModificacion(LocalDate.now())
                .build();
    }

    public Cliente darDeBaja() {
        if (!this.activo) {
            throw new BusinessRunTimeException(BusinessErrors.clienteYaDadoDeBaja());
        }
        return this.toBuilder()
                .activo(false)
                .fechaModificacion(LocalDate.now())
                .build();
    }

}
