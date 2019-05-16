package ru.bublig.testtask.view;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.bublig.testtask.component.DoctorEditor;
import ru.bublig.testtask.config.HSQLDBConnection;
import ru.bublig.testtask.model.Doctor;
import ru.bublig.testtask.service.DoctorService;

@Theme("mytheme")
public class DoctorView extends UI {

    private final DoctorService doctorService = new DoctorService(HSQLDBConnection.getInstance());
    private Grid<Doctor> doctorGrid = new Grid<>(Doctor.class);

    private final TextField filterText = new TextField();
    private final Button addNewBtn = new Button("Add new doctor");
    private final DoctorEditor doctorEditor = new DoctorEditor(this);

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();

        filterText.addValueChangeListener(clickEvent -> updateList());
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.setPlaceholder("Filter by name");

        Button clearFilterTextBtn = new Button(VaadinIcons.CLOSE);
        clearFilterTextBtn.setDescription("Clear the current input");
        clearFilterTextBtn.addClickListener(clickEvent -> filterText.clear());

        CssLayout filtering = new CssLayout();
        filtering.addComponents(filterText, clearFilterTextBtn);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        addNewBtn.addClickListener(clickEvent -> {
            doctorGrid.asSingleSelect().clear();
            doctorEditor.setDoctor(new Doctor());
        });

        HorizontalLayout toolbar = new HorizontalLayout(filtering, addNewBtn);
        HorizontalLayout main = new HorizontalLayout(doctorGrid, doctorEditor);

        doctorGrid.setColumns("id", "lastName", "firstName", "patronymic", "specialization");

        main.setSizeFull();
        doctorGrid.setSizeFull();

        main.setExpandRatio(doctorGrid, 1);

        layout.addComponents(toolbar, main);

        updateList();

        setContent(layout);

        doctorEditor.setVisible(false);

        doctorGrid.asSingleSelect().addValueChangeListener(valueChangeEvent -> {
            if (valueChangeEvent.getValue() == null) {
                doctorEditor.setVisible(false);
            } else
                doctorEditor.setDoctor(valueChangeEvent.getValue());
        });
    }

    public void updateList() {
        doctorGrid.setItems(doctorService.getAll(filterText.getValue()));
    }
}
