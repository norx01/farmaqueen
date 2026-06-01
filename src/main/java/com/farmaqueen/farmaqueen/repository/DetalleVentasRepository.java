package com.farmaqueen.farmaqueen.repository;

import com.farmaqueen.farmaqueen.model.DetalleVentas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DetalleVentasRepository extends JpaRepository<DetalleVentas, Long>
{
    @Query("""
            SELECT d.producto.nombre, SUM(d.cantidad), SUM(d.subtotal)
            FROM DetalleVentas d
            WHERE d.venta.fecha_hora >= :inicio AND d.venta.fecha_hora < :fin
            GROUP BY d.producto.id_producto, d.producto.nombre
            ORDER BY SUM(d.cantidad) DESC
            """)
    List<Object[]> productosMasVendidos(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}
