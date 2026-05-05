package com.farmaqueen.farmaqueen.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuarios
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_usuarios;

    @NotBlank( message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank( message = "El documento es obligatorio")
    private String documento;

    @NotBlank( message = "El telefono es obligatorio")
    private String telefono;

    @NotBlank( message = "El correo es obligatorio")
    private String correo;

    @NotBlank( message = "La direccion es obligatoria")
    private String direccion;

    @NotBlank( message = "La contraseña es obligatoria")
    private String contrasena;

    @NotBlank( message = "El tipo de documento es obligatorio")
    private String tipo_documento;

    @NotNull( message = "La fecha de nacimiento es obligatoria")
    private LocalDate fecha_nacimiento;

    @NotBlank( message = "Las observaciones son obligatorias")
    private String observaciones;
}
