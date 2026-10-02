package com.ejemplo.servlet;

import com.ejemplo.model.Tarea;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "DetalleTareaServlet", urlPatterns = {"/tareas/detalle"})
public class DetalleTareaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id;
        try {
            id = Integer.parseInt(req.getParameter("id"));
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/tareas?error=id-invalido");
            return;
        }

        // La lista se lee de applicationScope, donde TareasServlet la publicó
        // en su init(), en vez de mantener una copia propia en este Servlet
        @SuppressWarnings("unchecked")
        List<Tarea> tareas = (List<Tarea>) getServletContext().getAttribute("tareas");

        Tarea tarea = tareas == null ? null : tareas.stream()
            .filter(t -> t.getId() == id)
            .findFirst()
            .orElse(null);

        if (tarea == null) {
            resp.sendRedirect(req.getContextPath() + "/tareas?error=no-encontrada");
            return;
        }

        // Se usa forward (no redirect): el objeto Tarea viaja en el request
        // hacia detalle.jsp sin generar una nueva petición del navegador
        req.setAttribute("tarea", tarea);
        req.getRequestDispatcher("/WEB-INF/views/detalle.jsp")
           .forward(req, resp);
    }
}
