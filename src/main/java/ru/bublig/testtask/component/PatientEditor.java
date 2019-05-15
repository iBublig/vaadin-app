package ru.bublig.testtask.component;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import ru.bublig.testtask.config.HSQLDBConnection;
import ru.bublig.testtask.model.Patient;
import ru.bublig.testtask.service.PatientService;
import ru.bublig.testtask.view.PatientView;

public class PatientEditor extends FormLayout {
    private final PatientService patientService;

    private Patient patient;

    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextField patronymic = new TextField("Patronymic");
    private TextField phone = new TextField("Phone");

    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete");

    private Binder<Patient> binder = new Binder<>(Patient.class);

    private PatientView patientView;

    public PatientEditor(PatientView patientView) {
        this.patientView = patientView;
        patientService = new PatientService(HSQLDBConnection.getInstance());

        setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout(save, cancel, delete);
        addComponents(firstName, lastName, patronymic, phone, buttons);

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        binder.bindInstanceFields(this);

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
        binder.setBean(patient);

        delete.setVisible(patient.isPersisted());
        setVisible(true);
        firstName.selectAll();
    }

    private void delete() {
        patientService.delete(patient.getId());
        patientView.updateList();
        setVisible(false);
    }

    private void save() {
        patientService.save(patient);
        patientView.updateList();
        setVisible(false);
    }
}
