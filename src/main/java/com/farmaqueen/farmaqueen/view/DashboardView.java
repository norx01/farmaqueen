package com.farmaqueen.farmaqueen.view;

import com.farmaqueen.farmaqueen.repository.ClientesRepository;
import com.farmaqueen.farmaqueen.repository.ProductosRepository;
import com.farmaqueen.farmaqueen.repository.UsuariosRepository;
import com.farmaqueen.farmaqueen.repository.VentasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
public class DashboardView
{
    @Autowired
    private VentasRepository ventasRepository;

    @Autowired
    private ProductosRepository productosRepository;

    @Autowired
    private ClientesRepository clientesRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model)
    {
        LocalDateTime inicioDia = LocalDate.now().atStartOfDay();
        LocalDateTime finDia = inicioDia.plusDays(1);

        BigDecimal totalGeneral = ventasRepository.totalVentas();
        BigDecimal totalDia = ventasRepository.totalVentasEntre(inicioDia, finDia);

        model.addAttribute("totalGeneral", totalGeneral);
        model.addAttribute("totalDia", totalDia);
        model.addAttribute("totalVentas", ventasRepository.count());
        model.addAttribute("totalProductos", productosRepository.count());
        model.addAttribute("totalClientes", clientesRepository.count());
        model.addAttribute("totalUsuarios", usuariosRepository.count());
        model.addAttribute("ventasRecientes", ventasRepository.ventasRecientes());
        model.addAttribute("productosStockBajo", productosRepository.productosConStockBajo());

        return "dashboard/index";
    }
}
