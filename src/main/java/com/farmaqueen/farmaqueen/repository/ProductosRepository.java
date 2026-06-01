package com.farmaqueen.farmaqueen.repository;

import com.farmaqueen.farmaqueen.model.Productos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductosRepository extends JpaRepository<Productos, Long>
{
    @Query("SELECT p FROM Productos p WHERE p.stock <= p.stock_minimo ORDER BY p.stock ASC")
    List<Productos> productosConStockBajo();
}
