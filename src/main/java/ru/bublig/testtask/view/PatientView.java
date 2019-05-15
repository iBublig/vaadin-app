package ru.bublig.testtask.view;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.bublig.testtask.component.PatientEditor;
import ru.bublig.testtask.config.HSQLDBConnection;
import ru.bublig.testtask.model.Doctor;
import ru.bublig.testtask.model.Patient;
import ru.bublig.testtask.service.PatientService;

@Theme("mytheme")
public class PatientView extends UI {
    private final PatientService patientService = new PatientService(HSQLDBConnection.getInstance());
    private Grid<Patient> patientGrid = new Grid<>(Patient.class);

    private final TextField filterText = new TextField();
    private final Button addNewBtn = new Button( "Add new patient");
    private final PatientEditor patientEditor = new PatientEditor(this);

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
        });

        HorizontalLayout toolbar = new HorizontalLayout(filtering, addNewBtn);
        HorizontalLayout main = new HorizontalLayout(patientGrid, patientEditor);

        patientGrid.setColumns("id", "firstName", "lastName", "patronymic", "phone");

        main.setSizeFull();
        patientGrid.setSizeFull();

        main.setExpandRatio(patientGrid, 1);

        layout.addComponents(toolbar, main);

        updateList();

        setContent(layout);

        patientEditor.setVisible(false);

        patientGrid.asSingleSelect().addValueChangeListener(valueChangeEvent -> {
            if (valueChangeEvent.getValue() == null) {
                patientEditor.setVisible(false);
            } else
                patientEditor.setPatient(valueChangeEvent.getValue());
        });
    }

    public void updateList() {
        patientGrid.setItems(patientService.getAll(filterText.getValue()));
    }
}
