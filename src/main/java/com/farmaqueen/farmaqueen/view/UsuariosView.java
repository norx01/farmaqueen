package com.farmaqueen.farmaqueen.view;

import com.farmaqueen.farmaqueen.model.Usuarios;
import com.farmaqueen.farmaqueen.repository.UsuariosRepository;
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
public class UsuariosView
{
    @Autowired
    private UsuariosRepository usuariosRepository;

    @GetMapping("/view/usuarios")
    public String lista(Model model)
    {
        model.addAttribute("usuarios", usuariosRepository.findAll());
        return "usuarios/list";
    }

    @GetMapping("/view/usuarios/form")
    public String form(Model model)
    {
        model.addAttribute("usuarios", new Usuarios());
        return "usuarios/form";
    }

    @PostMapping("/view/usuarios/save")
    public String save(@ModelAttribute Usuarios usuarios, RedirectAttributes ra)
    {
        usuariosRepository.save(usuarios);
        ra.addFlashAttribute("message", "Usuario guardado con exito");
        return "redirect:/view/usuarios";
    }

    @GetMapping("/view/usuarios/edit/{id}")
    public String edit(@PathVariable Long id, Model model)
    {
        Usuarios usuarios =  usuariosRepository.findById(id).orElse(null);
        model.addAttribute("usuarios", usuarios);
        return "usuarios/form";
    }

    @PostMapping("/view/usuarios/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra)
    {
        usuariosRepository.deleteById(id);
        ra.addFlashAttribute("message", "Usuario eliminado con exito");
        return "redirect:/view/usuarios";
    }

    @GetMapping("/view/usuarios/pdf")
    public void exportarPDF(HttpServletResponse response) throws Exception
    {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition","attachment; filename=Usuarios.pdf");

        List<Usuarios> usuariosList = usuariosRepository.findAll();

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        document.add(new Paragraph("Listado de Usuarios"));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(10);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        //Columnas
        table.addCell("ID Usuario");
        table.addCell("Nombre");
        table.addCell("Tipo de documento");
        table.addCell("Documento");
        table.addCell("Telefono");
        table.addCell("Correo");
        table.addCell("Direccion");
        table.addCell("Contraseña");
        table.addCell("Fecha de nacimiento");
        table.addCell("Observaciones");

        //filas
        for (Usuarios usuarios : usuariosList)
        {
            table.addCell(usuarios.getId_usuarios().toString());
            table.addCell(usuarios.getNombre());
            table.addCell(usuarios.getTipo_documento());
            table.addCell(usuarios.getDocumento());
            table.addCell(usuarios.getTelefono());
            table.addCell(usuarios.getCorreo());
            table.addCell(usuarios.getDireccion());
            table.addCell(usuarios.getContrasena());
            table.addCell(usuarios.getFecha_nacimiento().toString());
            table.addCell(usuarios.getObservaciones());
        }

        document.add(table);
        document.close();
    }

    @GetMapping("/view/usuarios/excel")
    public void exportarExcel(HttpServletResponse response) throws Exception
    {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=Usuarios.xlsx");

        List<Usuarios> usuariosList = usuariosRepository.findAll(); // Reemplaza con tu repositorio

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Usuarios");

        // Crear encabezado
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID Usuarios");
        headerRow.createCell(1).setCellValue("Nombre");
        headerRow.createCell(2).setCellValue("Tipo de documento");
        headerRow.createCell(3).setCellValue("Documento");
        headerRow.createCell(4).setCellValue("Telefono");
        headerRow.createCell(5).setCellValue("Correo");
        headerRow.createCell(6).setCellValue("Direccion");
        headerRow.createCell(7).setCellValue("Contraseña");
        headerRow.createCell(8).setCellValue("Fecha de nacimiento");
        headerRow.createCell(9).setCellValue("Observaciones");

        // Agregar datos
        int rowNum = 1;
        for (Usuarios f : usuariosList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(f.getId_usuarios());
            row.createCell(1).setCellValue(f.getNombre());
            row.createCell(2).setCellValue(f.getTipo_documento());
            row.createCell(3).setCellValue(f.getDocumento());
            row.createCell(4).setCellValue(f.getTelefono());
            row.createCell(5).setCellValue(f.getCorreo());
            row.createCell(6).setCellValue(f.getDireccion());
            row.createCell(7).setCellValue(f.getContrasena());
            row.createCell(8).setCellValue(f.getFecha_nacimiento());
            row.createCell(9).setCellValue(f.getObservaciones());
        }

        // Autoajustar columnas
        for (int i = 0; i < 10; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
