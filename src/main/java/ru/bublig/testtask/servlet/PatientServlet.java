package ru.bublig.testtask.servlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import ru.bublig.testtask.view.PatientView;

import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/patient/*", name = "Patient", asyncSupported = true)
@VaadinServletConfiguration(ui = PatientView.class, productionMode = false)
public class PatientServlet extends VaadinServlet {
}
