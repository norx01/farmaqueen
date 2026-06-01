package com.farmaqueen.farmaqueen.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "detalle_ventas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleVentas
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_detalle_venta;

    @ManyToOne
    @JoinColumn(name = "id_venta")
    @NotNull(message = "La venta es obligatoria")
    @JsonIgnore
    private Ventas venta;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    @NotNull(message = "El producto es obligatorio")
    private Productos producto;

    @NotNull(message = "La cantidad es obligatoria")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    private BigDecimal precio_unitario;

    @NotNull(message = "El subtotal es obligatorio")
    private BigDecimal subtotal;
}
