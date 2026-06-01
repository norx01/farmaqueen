package com.farmaqueen.farmaqueen.config;

import com.farmaqueen.farmaqueen.repository.UsuariosRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor
{
    @Autowired
    private UsuariosRepository usuariosRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        String path = request.getRequestURI();

        if (path.equals("/login")
                || path.equals("/logout")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.startsWith("/api/")) {
            return true;
        }

        if (usuariosRepository.count() == 0 && (
                path.equals("/view/usuarios")
                        || path.equals("/view/usuarios/form")
                        || path.equals("/view/usuarios/save")
                        || path.startsWith("/view/usuarios/edit/")
                        || path.startsWith("/view/usuarios/delete/"))) {
            return true;
        }

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("usuarioLogueado") != null) {
            return true;
        }

        response.sendRedirect("/login");
        return false;
    }
}
