package com.farmaqueen.farmaqueen.view;

import com.farmaqueen.farmaqueen.model.Usuarios;
import com.farmaqueen.farmaqueen.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("usuarios", usuariosRepository.findById(id).orElse(null));
        return "usuarios/form";
    }

    @PostMapping("/view/usuarios/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra)
    {
        usuariosRepository.deleteById(id);
        ra.addFlashAttribute("message", "Usuario eliminado con exito");
        return "redirect:/view/usuarios";
    }
}
