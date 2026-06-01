package com.farmaqueen.farmaqueen.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Productos
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_producto;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El codigo es obligatorio")
    private String codigo;

    @NotNull(message = "El precio es obligatorio")
    private BigDecimal precio;

    @NotNull(message = "La fecha de fabricacion es obligatoria")
    private LocalDate fecha_fabricacion;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    private LocalDate fecha_vencimiento;

    @NotBlank(message = "El lote es obligatorio")
    private String lote;

    @NotNull(message = "El stock es obligatorio")
    private Integer stock;

    @NotNull(message = "El stock minimo es obligatorio")
    private Integer stock_minimo;
}
