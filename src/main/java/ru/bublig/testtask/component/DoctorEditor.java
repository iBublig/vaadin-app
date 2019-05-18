package ru.bublig.testtask.component;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.bublig.testtask.config.HSQLDBConnection;
import ru.bublig.testtask.model.Doctor;
import ru.bublig.testtask.service.DoctorService;
import ru.bublig.testtask.view.DoctorView;

public class DoctorEditor extends FormLayout {
    private final DoctorService doctorService;

    private Doctor doctor;

    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextField patronymic = new TextField("Patronymic");
    private TextField specialization = new TextField("Specialization");

    private Label error = new Label("Нельзя удалить, есть зависимость с рецептом");

    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete");

    private Binder<Doctor> binder = new Binder<>(Doctor.class);

    private DoctorView doctorView;

    public DoctorEditor(DoctorView doctorView) {
        this.doctorView = doctorView;
        doctorService = new DoctorService(HSQLDBConnection.getInstance());

        error.setVisible(false);

        setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout(save, cancel, delete);
        addComponents(firstName, lastName, patronymic, specialization, buttons, error);

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        binder.bindInstanceFields(this);

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> setVisible(false));
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
        binder.setBean(doctor);

        delete.setVisible(doctor.isPersisted());
        setVisible(true);
        firstName.selectAll();
    }

    private void delete() {
        if (!doctorService.delete(doctor.getId())){
            error.setVisible(true);
            return;
        }
        doctorView.updateList();
        setVisible(false);
    }

    private void save() {
        doctorService.save(doctor);
        doctorView.updateList();
        setVisible(false);
    }
}
