package com.farmaqueen.farmaqueen.view;

import com.farmaqueen.farmaqueen.model.Clientes;
import com.farmaqueen.farmaqueen.repository.ClientesRepository;
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
public class ClientesView
{
    @Autowired
    private ClientesRepository clientesRepository;

    @GetMapping("/view/clientes")
    public String lista(Model model)
    {
        model.addAttribute("clientes", clientesRepository.findAll());
        return "clientes/list";
    }

    @GetMapping("/view/clientes/form")
    public String form(Model model)
    {
        model.addAttribute("clientes", new Clientes());
        return "clientes/form";
    }

    @PostMapping("/view/clientes/save")
    public String save(@ModelAttribute Clientes clientes, RedirectAttributes ra)
    {
        clientesRepository.save(clientes);
        ra.addFlashAttribute("message", "Cliente guardado con exito");
        return "redirect:/view/clientes";
    }

    @GetMapping("/view/clientes/edit/{id}")
    public String edit(@PathVariable Long id, Model model)
    {
        Clientes clientes = clientesRepository.findById(id).orElse(null);
        model.addAttribute("clientes", clientes);
        return "clientes/form";
    }

    @PostMapping("/view/clientes/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra)
    {
        clientesRepository.deleteById(id);
        ra.addFlashAttribute("message", "Cliente eliminado con exito");
        return "redirect:/view/clientes";
    }

    @GetMapping("/view/clientes/pdf")
    public void exportarPDF(HttpServletResponse response) throws Exception
    {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Clientes.pdf");

        List<Clientes> clientesList = clientesRepository.findAll();

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        document.add(new Paragraph("Listado de Clientes"));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        table.addCell("ID Cliente");
        table.addCell("Nombre");
        table.addCell("Tipo de documento");
        table.addCell("Documento");
        table.addCell("Direccion");
        table.addCell("Telefono");
        table.addCell("Correo");

        for (Clientes clientes : clientesList)
        {
            table.addCell(clientes.getId_cliente().toString());
            table.addCell(clientes.getNombre());
            table.addCell(clientes.getTipo_documento());
            table.addCell(clientes.getDocumento());
            table.addCell(clientes.getDireccion());
            table.addCell(clientes.getTelefono());
            table.addCell(clientes.getCorreo());
        }

        document.add(table);
        document.close();
    }

    @GetMapping("/view/clientes/excel")
    public void exportarExcel(HttpServletResponse response) throws Exception
    {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Clientes.xlsx");

        List<Clientes> clientesList = clientesRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Clientes");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID Cliente");
        headerRow.createCell(1).setCellValue("Nombre");
        headerRow.createCell(2).setCellValue("Tipo de documento");
        headerRow.createCell(3).setCellValue("Documento");
        headerRow.createCell(4).setCellValue("Direccion");
        headerRow.createCell(5).setCellValue("Telefono");
        headerRow.createCell(6).setCellValue("Correo");

        int rowNum = 1;
        for (Clientes c : clientesList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(c.getId_cliente());
            row.createCell(1).setCellValue(c.getNombre());
            row.createCell(2).setCellValue(c.getTipo_documento());
            row.createCell(3).setCellValue(c.getDocumento());
            row.createCell(4).setCellValue(c.getDireccion());
            row.createCell(5).setCellValue(c.getTelefono());
            row.createCell(6).setCellValue(c.getCorreo());
        }

        for (int i = 0; i < 7; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
