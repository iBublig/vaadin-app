package ru.bublig.testtask.view;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.bublig.testtask.component.DoctorWindowEditor;
import ru.bublig.testtask.component.StatisticsWindow;
import ru.bublig.testtask.config.HSQLDBConnection;
import ru.bublig.testtask.model.Doctor;
import ru.bublig.testtask.service.DoctorService;

@Theme("mytheme")
public class DoctorView extends UI {

    private final DoctorService doctorService = new DoctorService(HSQLDBConnection.getInstance());
    private Grid<Doctor> doctorGrid = new Grid<>(Doctor.class);

    private final TextField filterText = new TextField();
    private final Button addNewBtn = new Button("Add new doctor");
    private final Button statisticsButton = new Button("Statistics");
    private final DoctorWindowEditor doctorWindowEditor = new DoctorWindowEditor(this);
    private final StatisticsWindow statisticsWindow = new StatisticsWindow();

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
            doctorWindowEditor.setDoctor(new Doctor());
            UI.getCurrent().addWindow(doctorWindowEditor);
        });

        statisticsButton.addClickListener(clickEvent -> {
            UI.getCurrent().addWindow(statisticsWindow);
        });

        HorizontalLayout toolbar = new HorizontalLayout(filtering, addNewBtn, statisticsButton);
        HorizontalLayout main = new HorizontalLayout(doctorGrid);

        doctorGrid.setColumns("id", "lastName", "firstName", "patronymic", "specialization");

        main.setSizeFull();
        doctorGrid.setSizeFull();

        main.setExpandRatio(doctorGrid, 1);

        layout.addComponents(toolbar, main);

        updateList();

        setContent(layout);

        doctorGrid.asSingleSelect().addValueChangeListener(valueChangeEvent -> {
            if (!(valueChangeEvent.getValue() == null)) {
                doctorWindowEditor.setDoctor(valueChangeEvent.getValue());
                UI.getCurrent().addWindow(doctorWindowEditor);
            }
        });
    }

    public void updateList() {
        deselectAll();
        doctorGrid.setItems(doctorService.getAll(filterText.getValue()));
    }

    public void deselectAll() {
        doctorGrid.deselectAll();
    }
}
