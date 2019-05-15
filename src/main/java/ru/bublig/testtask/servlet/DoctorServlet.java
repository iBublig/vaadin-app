package ru.bublig.testtask.servlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import ru.bublig.testtask.view.DoctorView;

import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/doctor/*", name = "Doctor", asyncSupported = true)
@VaadinServletConfiguration(ui = DoctorView.class, productionMode = false)
public class DoctorServlet extends VaadinServlet {
}
