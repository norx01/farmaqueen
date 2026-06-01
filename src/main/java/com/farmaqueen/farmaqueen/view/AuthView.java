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

import java.util.Optional;

@Controller
public class AuthView
{
    @Autowired
    private UsuariosRepository usuariosRepository;

    @GetMapping("/login")
    public String login(Model model, HttpSession session)
    {
        if (session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/dashboard";
        }

        model.addAttribute("sinUsuarios", usuariosRepository.count() == 0);
        return "auth/login";
    }

    @PostMapping("/login")
    public String authenticate(@RequestParam String correo,
                               @RequestParam String contrasena,
                               HttpSession session,
                               RedirectAttributes ra)
    {
        Optional<Usuarios> usuario = usuariosRepository.findByCorreoAndContrasena(correo, contrasena);

        if (usuario.isEmpty()) {
            ra.addFlashAttribute("error", "Correo o contraseña incorrectos");
            return "redirect:/login";
        }

        session.setAttribute("usuarioLogueado", usuario.get());
        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes ra)
    {
        session.invalidate();
        ra.addFlashAttribute("message", "Sesión cerrada correctamente");
        return "redirect:/login";
    }
}
