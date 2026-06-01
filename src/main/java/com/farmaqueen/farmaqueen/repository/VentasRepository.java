package com.farmaqueen.farmaqueen.repository;

import com.farmaqueen.farmaqueen.model.Ventas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface VentasRepository extends JpaRepository<Ventas, Long>
{
    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Ventas v")
    BigDecimal totalVentas();

    @Query("SELECT COALESCE(SUM(v.total), 0) FROM Ventas v WHERE v.fecha_hora >= :inicio AND v.fecha_hora < :fin")
    BigDecimal totalVentasEntre(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT v FROM Ventas v WHERE v.fecha_hora >= :inicio AND v.fecha_hora < :fin ORDER BY v.fecha_hora DESC")
    List<Ventas> ventasEntre(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT v FROM Ventas v ORDER BY v.fecha_hora DESC")
    List<Ventas> ventasRecientes();
}
