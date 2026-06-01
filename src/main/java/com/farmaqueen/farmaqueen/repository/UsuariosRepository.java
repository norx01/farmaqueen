package com.farmaqueen.farmaqueen.repository;

import com.farmaqueen.farmaqueen.model.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuarios, Long>
{
    Optional<Usuarios> findByCorreoAndContrasena(String correo, String contrasena);
}
