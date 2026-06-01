package com.farmaqueen.farmaqueen.controller;

import com.farmaqueen.farmaqueen.model.Ventas;
import com.farmaqueen.farmaqueen.repository.VentasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentasController
{
    @Autowired
    private VentasRepository ventasRepository;

    @GetMapping
    public List<Ventas> getAll()
    {
        return ventasRepository.findAll();
    }

    @GetMapping("/{id}")
    public Ventas getById(@PathVariable Long id)
    {
        return ventasRepository.findById(id).orElse(null);
    }
}
