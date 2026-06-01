package com.farmaqueen.farmaqueen.view;

import com.farmaqueen.farmaqueen.model.Productos;
import com.farmaqueen.farmaqueen.model.Ventas;
import com.farmaqueen.farmaqueen.repository.ClientesRepository;
import com.farmaqueen.farmaqueen.repository.DetalleVentasRepository;
import com.farmaqueen.farmaqueen.repository.ProductosRepository;
import com.farmaqueen.farmaqueen.repository.VentasRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ReportesView
{
    @Autowired
    private VentasRepository ventasRepository;

    @Autowired
    private DetalleVentasRepository detalleVentasRepository;

    @Autowired
    private ProductosRepository productosRepository;

    @Autowired
    private ClientesRepository clientesRepository;

    @GetMapping("/reportes")
    public String reportes(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
                           Model model)
    {
        LocalDate inicio = fechaInicio != null ? fechaInicio : LocalDate.now().withDayOfMonth(1);
        LocalDate fin = fechaFin != null ? fechaFin : LocalDate.now();
        LocalDateTime inicioFecha = inicio.atStartOfDay();
        LocalDateTime finFecha = fin.plusDays(1).atStartOfDay();

        List<Ventas> ventas = ventasRepository.ventasEntre(inicioFecha, finFecha);
        BigDecimal totalVentas = ventasRepository.totalVentasEntre(inicioFecha, finFecha);
        BigDecimal ticketPromedio = ventas.isEmpty()
                ? BigDecimal.ZERO
                : totalVentas.divide(BigDecimal.valueOf(ventas.size()), 2, RoundingMode.HALF_UP);

        model.addAttribute("fechaInicio", inicio);
        model.addAttribute("fechaFin", fin);
        model.addAttribute("ventas", ventas);
        model.addAttribute("totalVentas", totalVentas);
        model.addAttribute("cantidadVentas", ventas.size());
        model.addAttribute("ticketPromedio", ticketPromedio);
        model.addAttribute("totalClientes", clientesRepository.count());
        model.addAttribute("totalProductos", productosRepository.count());
        model.addAttribute("productosStockBajo", productosRepository.productosConStockBajo());
        model.addAttribute("productosMasVendidos", detalleVentasRepository.productosMasVendidos(inicioFecha, finFecha));

        return "reportes/index";
    }

    @GetMapping("/reportes/pdf")
    public void exportarPDF(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
                            HttpServletResponse response) throws Exception
    {
        LocalDate inicio = fechaInicio != null ? fechaInicio : LocalDate.now().withDayOfMonth(1);
        LocalDate fin = fechaFin != null ? fechaFin : LocalDate.now();
        LocalDateTime inicioFecha = inicio.atStartOfDay();
        LocalDateTime finFecha = fin.plusDays(1).atStartOfDay();
        List<Ventas> ventas = ventasRepository.ventasEntre(inicioFecha, finFecha);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Reporte_Ventas.pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        document.add(new Paragraph("Reporte de Ventas"));
        document.add(new Paragraph("Rango: " + inicio + " a " + fin));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        table.addCell("ID");
        table.addCell("Cliente");
        table.addCell("Fecha");
        table.addCell("Medio de pago");
        table.addCell("Productos");
        table.addCell("Total");

        for (Ventas venta : ventas)
        {
            table.addCell(venta.getId_venta().toString());
            table.addCell(venta.getCliente().getNombre());
            table.addCell(venta.getFecha_hora().toString());
            table.addCell(venta.getMedio_pago());
            table.addCell(String.valueOf(venta.getDetalles().size()));
            table.addCell(venta.getTotal().toString());
        }

        document.add(table);
        document.close();
    }

    @GetMapping("/reportes/excel")
    public void exportarExcel(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
                              HttpServletResponse response) throws Exception
    {
        LocalDate inicio = fechaInicio != null ? fechaInicio : LocalDate.now().withDayOfMonth(1);
        LocalDate fin = fechaFin != null ? fechaFin : LocalDate.now();
        LocalDateTime inicioFecha = inicio.atStartOfDay();
        LocalDateTime finFecha = fin.plusDays(1).atStartOfDay();
        List<Ventas> ventas = ventasRepository.ventasEntre(inicioFecha, finFecha);
        List<Productos> stockBajo = productosRepository.productosConStockBajo();

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Reporte_Ventas.xlsx");

        Workbook workbook = new XSSFWorkbook();
        Sheet ventasSheet = workbook.createSheet("Ventas");
        Sheet stockSheet = workbook.createSheet("Stock bajo");

        Row headerRow = ventasSheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Cliente");
        headerRow.createCell(2).setCellValue("Fecha");
        headerRow.createCell(3).setCellValue("Medio de pago");
        headerRow.createCell(4).setCellValue("Productos");
        headerRow.createCell(5).setCellValue("Total");

        int rowNum = 1;
        for (Ventas venta : ventas) {
            Row row = ventasSheet.createRow(rowNum++);
            row.createCell(0).setCellValue(venta.getId_venta());
            row.createCell(1).setCellValue(venta.getCliente().getNombre());
            row.createCell(2).setCellValue(venta.getFecha_hora().toString());
            row.createCell(3).setCellValue(venta.getMedio_pago());
            row.createCell(4).setCellValue(venta.getDetalles().size());
            row.createCell(5).setCellValue(venta.getTotal().doubleValue());
        }

        Row stockHeader = stockSheet.createRow(0);
        stockHeader.createCell(0).setCellValue("Producto");
        stockHeader.createCell(1).setCellValue("Codigo");
        stockHeader.createCell(2).setCellValue("Stock");
        stockHeader.createCell(3).setCellValue("Stock minimo");

        int stockRowNum = 1;
        for (Productos producto : stockBajo) {
            Row row = stockSheet.createRow(stockRowNum++);
            row.createCell(0).setCellValue(producto.getNombre());
            row.createCell(1).setCellValue(producto.getCodigo());
            row.createCell(2).setCellValue(producto.getStock());
            row.createCell(3).setCellValue(producto.getStock_minimo());
        }

        for (int i = 0; i < 6; i++) {
            ventasSheet.autoSizeColumn(i);
        }

        for (int i = 0; i < 4; i++) {
            stockSheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
