package com.farmaqueen.farmaqueen.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ventas
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_venta;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    @NotNull(message = "El cliente es obligatorio")
    private Clientes cliente;

    @NotNull(message = "La fecha y hora son obligatorias")
    private LocalDateTime fecha_hora;

    @NotBlank(message = "El medio de pago es obligatorio")
    private String medio_pago;

    @NotNull(message = "El total es obligatorio")
    private BigDecimal total;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<DetalleVentas> detalles = new ArrayList<>();
}
