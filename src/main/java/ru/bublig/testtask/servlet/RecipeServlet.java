package ru.bublig.testtask.servlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinServlet;
import ru.bublig.testtask.view.RecipeView;

import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/recipe/*", name = "Recipe", asyncSupported = true)
@VaadinServletConfiguration(ui = RecipeView.class, productionMode = false)
public class RecipeServlet extends VaadinServlet {
}
