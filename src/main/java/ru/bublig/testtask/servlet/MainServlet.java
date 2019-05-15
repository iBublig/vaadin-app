package ru.bublig.testtask.servlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import ru.bublig.testtask.view.MainView;

import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
@VaadinServletConfiguration(ui = MainView.class, productionMode = false)
public class MainServlet extends VaadinServlet {
}
