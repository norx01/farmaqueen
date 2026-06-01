package com.farmaqueen.farmaqueen.controller;

import com.farmaqueen.farmaqueen.model.Productos;
import com.farmaqueen.farmaqueen.repository.ProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductosController
{
    @Autowired
    private ProductosRepository productosRepository;

    @GetMapping
    public List<Productos> getAll()
    {
        return productosRepository.findAll();
    }

    @GetMapping("/{id}")
    public Productos getById(@PathVariable Long id)
    {
        return productosRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Productos save(@RequestBody Productos productos)
    {
        return productosRepository.save(productos);
    }

    @PutMapping("/{id}")
    public Productos update(@PathVariable Long id, @RequestBody Productos productos)
    {
        productos.setId_producto(id);
        return productosRepository.save(productos);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id)
    {
        productosRepository.deleteById(id);
    }
}
