package com.farmaqueen.farmaqueen.controller;

import com.farmaqueen.farmaqueen.model.Clientes;
import com.farmaqueen.farmaqueen.repository.ClientesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClientesController
{
    @Autowired
    private ClientesRepository clientesRepository;

    @GetMapping
    public List<Clientes> getAll()
    {
        return clientesRepository.findAll();
    }

    @GetMapping("/{id}")
    public Clientes getById(@PathVariable Long id)
    {
        return clientesRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Clientes save(@RequestBody Clientes clientes)
    {
        return clientesRepository.save(clientes);
    }

    @PutMapping("/{id}")
    public Clientes update(@PathVariable Long id, @RequestBody Clientes clientes)
    {
        clientes.setId_cliente(id);
        return clientesRepository.save(clientes);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id)
    {
        clientesRepository.deleteById(id);
    }
}
