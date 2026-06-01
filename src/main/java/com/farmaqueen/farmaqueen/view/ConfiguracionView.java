package com.farmaqueen.farmaqueen.view;

import com.farmaqueen.farmaqueen.model.Usuarios;
import com.farmaqueen.farmaqueen.repository.UsuariosRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ConfiguracionView
{
    @Autowired
    private UsuariosRepository usuariosRepository;

    @GetMapping("/configuracion")
    public String configuracion(Model model, HttpSession session)
    {
        Usuarios usuario = (Usuarios) session.getAttribute("usuarioLogueado");
        model.addAttribute("usuario", usuario);
        return "configuracion/index";
    }

    @PostMapping("/configuracion/save")
    public String save(@RequestParam String nombre,
                       @RequestParam String correo,
                       @RequestParam String telefono,
                       @RequestParam String direccion,
                       @RequestParam(required = false) String contrasena,
                       HttpSession session,
                       RedirectAttributes ra)
    {
        Usuarios usuarioSesion = (Usuarios) session.getAttribute("usuarioLogueado");
        Usuarios usuario = usuariosRepository.findById(usuarioSesion.getId_usuarios()).orElse(null);

        if (usuario == null) {
            session.invalidate();
            ra.addFlashAttribute("error", "La sesión ya no es válida");
            return "redirect:/login";
        }

        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setTelefono(telefono);
        usuario.setDireccion(direccion);

        if (contrasena != null && !contrasena.isBlank()) {
            usuario.setContrasena(contrasena);
        }

        usuariosRepository.save(usuario);
        session.setAttribute("usuarioLogueado", usuario);

        ra.addFlashAttribute("message", "Configuración actualizada correctamente");
        return "redirect:/configuracion";
    }
}
