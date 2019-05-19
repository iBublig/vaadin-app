package ru.bublig.testtask.view;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.*;

import java.io.File;

public class MainView extends UI {

    private final Link patient = new Link("Пациенты", new ExternalResource("./patient/"));
    private final Link doctor = new Link("Врачи", new ExternalResource("./doctor/"));
    private final Link recipe = new Link("Рецепты", new ExternalResource("./recipe/"));

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final HorizontalLayout layout = new HorizontalLayout(patient, doctor, recipe);

        final VerticalLayout main = new VerticalLayout(layout);
        main.setComponentAlignment(layout, Alignment.BOTTOM_CENTER);

        String basepath = VaadinService.getCurrent()
                .getBaseDirectory().getAbsolutePath();

        FileResource resource = new FileResource(new File(basepath +
                "/WEB-INF/images/hospital.png"));

        Image image = new Image("", resource);

        main.addComponent(image);

        setContent(main);
    }
}
