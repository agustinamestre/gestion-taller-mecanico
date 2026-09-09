package com.taller.gestion_taller.domain.model;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class Alerta {

    private Long id;
    private Long vehiculoId;
    private LocalDate fechaAlerta;
    private TipoAlerta tipo;
    private boolean contactado;
    private LocalDate fechaContacto;
    private MedioContacto medioContacto;
    private String observaciones;

    public static Alerta crearPorServiceVencido(Long vehiculoId) {
        return Alerta.builder()
                .vehiculoId(vehiculoId)
                .tipo(TipoAlerta.SERVICE_VENCIDO)
                .fechaAlerta(LocalDate.now())
                .contactado(false)
                .build();
    }

    public void marcarComoContactada(MedioContacto medioContacto, String observaciones) {
        if (this.contactado) {
            throw new BusinessRunTimeException(BusinessErrors.alertaYaContactada());
        }
        this.contactado = true;
        this.fechaContacto = LocalDate.now();
        this.medioContacto = medioContacto;
        this.observaciones = observaciones;
    }
}
