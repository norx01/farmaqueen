package com.farmaqueen.farmaqueen.view;

import com.farmaqueen.farmaqueen.model.Clientes;
import com.farmaqueen.farmaqueen.model.DetalleVentas;
import com.farmaqueen.farmaqueen.model.Productos;
import com.farmaqueen.farmaqueen.model.Ventas;
import com.farmaqueen.farmaqueen.repository.ClientesRepository;
import com.farmaqueen.farmaqueen.repository.ProductosRepository;
import com.farmaqueen.farmaqueen.repository.VentasRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class VentasView
{
    @Autowired
    private VentasRepository ventasRepository;

    @Autowired
    private ClientesRepository clientesRepository;

    @Autowired
    private ProductosRepository productosRepository;

    @GetMapping("/view/ventas")
    public String lista(Model model)
    {
        model.addAttribute("ventas", ventasRepository.findAll());
        return "ventas/list";
    }

    @GetMapping("/view/ventas/form")
    public String form(Model model)
    {
        model.addAttribute("clientes", clientesRepository.findAll());
        model.addAttribute("productos", productosRepository.findAll());
        return "ventas/form";
    }

    @GetMapping("/view/ventas/detail/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes ra)
    {
        Ventas venta = ventasRepository.findById(id).orElse(null);

        if (venta == null) {
            ra.addFlashAttribute("error", "La venta seleccionada no existe");
            return "redirect:/view/ventas";
        }

        model.addAttribute("venta", venta);
        return "ventas/detail";
    }

    @PostMapping("/view/ventas/save")
    @Transactional
    public String save(@RequestParam Long clienteId,
                       @RequestParam String medio_pago,
                       @RequestParam(name = "productoIds", required = false) List<Long> productoIds,
                       @RequestParam(name = "cantidades", required = false) List<Integer> cantidades,
                       RedirectAttributes ra)
    {
        if (productoIds == null || cantidades == null || productoIds.isEmpty() || cantidades.isEmpty()) {
            ra.addFlashAttribute("error", "Debe agregar al menos un producto a la venta");
            return "redirect:/view/ventas/form";
        }

        if (productoIds.size() != cantidades.size()) {
            ra.addFlashAttribute("error", "La información de productos no es válida");
            return "redirect:/view/ventas/form";
        }

        Clientes cliente = clientesRepository.findById(clienteId).orElse(null);

        if (cliente == null) {
            ra.addFlashAttribute("error", "El cliente seleccionado no existe");
            return "redirect:/view/ventas/form";
        }

        Ventas venta = Ventas.builder()
                .cliente(cliente)
                .fecha_hora(LocalDateTime.now())
                .medio_pago(medio_pago)
                .total(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i < productoIds.size(); i++) {
            Integer cantidad = cantidades.get(i);

            if (cantidad == null || cantidad <= 0) {
                ra.addFlashAttribute("error", "Todas las cantidades deben ser mayores a cero");
                return "redirect:/view/ventas/form";
            }

            Productos producto = productosRepository.findById(productoIds.get(i)).orElse(null);

            if (producto == null) {
                ra.addFlashAttribute("error", "Uno de los productos seleccionados no existe");
                return "redirect:/view/ventas/form";
            }

            if (producto.getStock() < cantidad) {
                ra.addFlashAttribute("error", "Stock insuficiente para " + producto.getNombre());
                return "redirect:/view/ventas/form";
            }

            BigDecimal subtotal = producto.getPrecio().multiply(BigDecimal.valueOf(cantidad));

            DetalleVentas detalle = DetalleVentas.builder()
                    .venta(venta)
                    .producto(producto)
                    .cantidad(cantidad)
                    .precio_unitario(producto.getPrecio())
                    .subtotal(subtotal)
                    .build();

            venta.getDetalles().add(detalle);
            producto.setStock(producto.getStock() - cantidad);
            productosRepository.save(producto);
            total = total.add(subtotal);
        }

        venta.setTotal(total);
        ventasRepository.save(venta);

        ra.addFlashAttribute("message", "Venta efectuada con exito");
        return "redirect:/view/ventas";
    }
}
