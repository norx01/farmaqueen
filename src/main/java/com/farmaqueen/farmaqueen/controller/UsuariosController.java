package com.farmaqueen.farmaqueen.controller;

import com.farmaqueen.farmaqueen.model.Usuarios;
import com.farmaqueen.farmaqueen.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuariosController
{
    @Autowired
    private UsuariosRepository usuariosRepository;

    @GetMapping
    public List<Usuarios> getAll()
    {
        return usuariosRepository.findAll();
    }

    @GetMapping("/{id}")
    public Usuarios getById(@PathVariable  Long id)
    {
        return usuariosRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Usuarios save(@RequestBody Usuarios usuarios)
    {
        return usuariosRepository.save(usuarios);
    }

    @PutMapping("/{id}")
    public Usuarios update(@PathVariable Long id, @RequestBody Usuarios usuarios)
    {
        usuarios.setId_usuarios(id);
        return usuariosRepository.save(usuarios);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id)
    {
        usuariosRepository.deleteById(id);
    }
}
