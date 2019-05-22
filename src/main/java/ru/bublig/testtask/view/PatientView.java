package ru.bublig.testtask.view;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.bublig.testtask.component.PatientWindowEditor;
import ru.bublig.testtask.config.HSQLDBConnection;
import ru.bublig.testtask.model.Patient;
import ru.bublig.testtask.service.PatientService;

@Theme("mytheme")
public class PatientView extends UI {
    private final PatientService patientService = new PatientService(HSQLDBConnection.getInstance());
    private Grid<Patient> patientGrid = new Grid<>(Patient.class);

    private final TextField filterText = new TextField();
    private final Button addNewBtn = new Button("Add new patient");
    private final PatientWindowEditor patientEditor = new PatientWindowEditor(this);

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
            patientGrid.asSingleSelect().clear();
            patientEditor.setPatient(new Patient());
            UI.getCurrent().addWindow(patientEditor);
        });

        HorizontalLayout toolbar = new HorizontalLayout(filtering, addNewBtn);
        HorizontalLayout main = new HorizontalLayout(patientGrid);

        patientGrid.setColumns("id", "lastName", "firstName", "patronymic", "phone");

        main.setSizeFull();
        patientGrid.setSizeFull();

        main.setExpandRatio(patientGrid, 1);

        layout.addComponents(toolbar, main);

        updateList();

        setContent(layout);

        patientGrid.asSingleSelect().addValueChangeListener(valueChangeEvent -> {
            if (!(valueChangeEvent.getValue() == null)) {
                patientEditor.setPatient(valueChangeEvent.getValue());
                UI.getCurrent().addWindow(patientEditor);
            }
        });
    }

    public void updateList() {
        deselectAll();
        patientGrid.setItems(patientService.getAll(filterText.getValue()));
    }

    public void deselectAll() {
        patientGrid.deselectAll();
    }
}
