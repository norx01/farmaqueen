package com.farmaqueen.farmaqueen.view;

import com.farmaqueen.farmaqueen.model.Productos;
import com.farmaqueen.farmaqueen.repository.ProductosRepository;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ProductosView
{
    @Autowired
    private ProductosRepository productosRepository;

    @GetMapping("/view/productos")
    public String lista(Model model)
    {
        model.addAttribute("productos", productosRepository.findAll());
        return "productos/list";
    }

    @GetMapping("/view/productos/form")
    public String form(Model model)
    {
        model.addAttribute("productos", new Productos());
        return "productos/form";
    }

    @PostMapping("/view/productos/save")
    public String save(@ModelAttribute Productos productos, RedirectAttributes ra)
    {
        productosRepository.save(productos);
        ra.addFlashAttribute("message", "Producto guardado con exito");
        return "redirect:/view/productos";
    }

    @GetMapping("/view/productos/edit/{id}")
    public String edit(@PathVariable Long id, Model model)
    {
        Productos productos = productosRepository.findById(id).orElse(null);
        model.addAttribute("productos", productos);
        return "productos/form";
    }

    @PostMapping("/view/productos/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra)
    {
        productosRepository.deleteById(id);
        ra.addFlashAttribute("message", "Producto eliminado con exito");
        return "redirect:/view/productos";
    }

    @GetMapping("/view/productos/pdf")
    public void exportarPDF(HttpServletResponse response) throws Exception
    {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Productos.pdf");

        List<Productos> productosList = productosRepository.findAll();

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        document.add(new Paragraph("Listado de Productos"));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        table.addCell("ID Producto");
        table.addCell("Nombre");
        table.addCell("Codigo");
        table.addCell("Precio");
        table.addCell("Fecha fabricacion");
        table.addCell("Fecha vencimiento");
        table.addCell("Lote");
        table.addCell("Stock");
        table.addCell("Stock minimo");

        for (Productos productos : productosList)
        {
            table.addCell(productos.getId_producto().toString());
            table.addCell(productos.getNombre());
            table.addCell(productos.getCodigo());
            table.addCell(productos.getPrecio().toString());
            table.addCell(productos.getFecha_fabricacion().toString());
            table.addCell(productos.getFecha_vencimiento().toString());
            table.addCell(productos.getLote());
            table.addCell(productos.getStock().toString());
            table.addCell(productos.getStock_minimo().toString());
        }

        document.add(table);
        document.close();
    }

    @GetMapping("/view/productos/excel")
    public void exportarExcel(HttpServletResponse response) throws Exception
    {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Productos.xlsx");

        List<Productos> productosList = productosRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Productos");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID Producto");
        headerRow.createCell(1).setCellValue("Nombre");
        headerRow.createCell(2).setCellValue("Codigo");
        headerRow.createCell(3).setCellValue("Precio");
        headerRow.createCell(4).setCellValue("Fecha fabricacion");
        headerRow.createCell(5).setCellValue("Fecha vencimiento");
        headerRow.createCell(6).setCellValue("Lote");
        headerRow.createCell(7).setCellValue("Stock");
        headerRow.createCell(8).setCellValue("Stock minimo");

        int rowNum = 1;
        for (Productos p : productosList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(p.getId_producto());
            row.createCell(1).setCellValue(p.getNombre());
            row.createCell(2).setCellValue(p.getCodigo());
            row.createCell(3).setCellValue(p.getPrecio().doubleValue());
            row.createCell(4).setCellValue(p.getFecha_fabricacion());
            row.createCell(5).setCellValue(p.getFecha_vencimiento());
            row.createCell(6).setCellValue(p.getLote());
            row.createCell(7).setCellValue(p.getStock());
            row.createCell(8).setCellValue(p.getStock_minimo());
        }

        for (int i = 0; i < 9; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
