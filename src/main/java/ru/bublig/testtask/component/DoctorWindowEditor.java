package ru.bublig.testtask.component;

import com.vaadin.data.Binder;
import com.vaadin.data.BinderValidationStatus;
import com.vaadin.data.BinderValidationStatusHandler;
import com.vaadin.data.HasValue;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.bublig.testtask.config.HSQLDBConnection;
import ru.bublig.testtask.model.Doctor;
import ru.bublig.testtask.service.DoctorService;
import ru.bublig.testtask.view.DoctorView;

public class DoctorWindowEditor extends Window {
    private static final String REGEXP = "^[а-яА-ЯёЁa-zA-Z ]+$";

    private final DoctorService doctorService;

    private Doctor doctor;

    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextField patronymic = new TextField("Patronymic");
    private TextField specialization = new TextField("Specialization");

    private Label error = new Label();

    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete");

    private Binder<Doctor> binder = new Binder<>(Doctor.class);

    private DoctorView doctorView;

    public DoctorWindowEditor(DoctorView doctorView) {
        super("Doctor");
        this.doctorView = doctorView;
        doctorService = new DoctorService(HSQLDBConnection.getInstance());

        initUi();
    }

    private void initUi() {
        center();
        setResizable(false);
        setModal(true);
        setWidth("300px");
        error.setVisible(false);

        HorizontalLayout buttons = new HorizontalLayout(save, cancel, delete);
        VerticalLayout window = new VerticalLayout(lastName, firstName, patronymic, specialization, buttons, error);
        setCenter(buttons, window);

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        binder.setValidationStatusHandler((BinderValidationStatusHandler<Doctor>) binderValidationStatus -> {
            if (binderValidationStatus.isOk())
                error.setVisible(false);
            else {
                String errorMessage = getValidationErrorMessage(binderValidationStatus);
                if (!errorMessage.equals("")) {
                    error.setValue(errorMessage);
                    error.setVisible(true);
                    setFocus(binderValidationStatus);
                }
            }
        });

        setValidation();

        binder.bindInstanceFields(this);

        save.addClickListener(e -> {
            BinderValidationStatus<Doctor> status = binder.validate();
            if (status.isOk()) {
                error.setVisible(false);
                save();
            } else {
                error.setVisible(true);
                error.setValue(getValidationErrorMessage(status));
            }
        });
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> close());
        setContent(window);
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
        binder.setBean(doctor);
        error.setVisible(false);

        delete.setVisible(doctor.isPersisted());
        lastName.selectAll();
    }

    private void delete() {
        if (!doctorService.delete(doctor.getId())) {
            error.setValue("Error: doctor in recipe");
            error.setVisible(true);
            return;
        }
        close();
    }

    private void setFocus(BinderValidationStatus<Doctor> binderValidationStatus) {
        HasValue field = binderValidationStatus.getFieldValidationErrors().get(0).getField();
        ((TextField) field).focus();
    }

    private void save() {
        doctorService.save(doctor);
        close();
    }

    private void setCenter(HorizontalLayout buttons, VerticalLayout window) {
        window.setComponentAlignment(lastName, Alignment.BOTTOM_CENTER);
        window.setComponentAlignment(firstName, Alignment.BOTTOM_CENTER);
        window.setComponentAlignment(patronymic, Alignment.BOTTOM_CENTER);
        window.setComponentAlignment(specialization, Alignment.BOTTOM_CENTER);
        window.setComponentAlignment(buttons, Alignment.BOTTOM_CENTER);
    }

    @Override
    public void close() {
        super.close();
        doctorView.updateList();
        doctorView.deselectAll();
    }

    private void setValidation() {
        binder.forField(firstName)
                .asRequired("!Empty field")
                .withValidator(new StringLengthValidator(
                        "Min 4 and max 35 character",
                        4, 35))
                .withValidator(new RegexpValidator(
                        "Fist name has invalid characters",
                        REGEXP))
                .bind(Doctor::getFirstName, Doctor::setFirstName);
        binder.forField(lastName)
                .asRequired("!Empty field")
                .withValidator(new StringLengthValidator(
                        "Min 4 and max 35 character",
                        4, 35))
                .withValidator(new RegexpValidator(
                        "Last name has invalid characters",
                        REGEXP))
                .bind(Doctor::getLastName, Doctor::setLastName);
        binder.forField(patronymic)
                .asRequired("!Empty field")
                .withValidator(new StringLengthValidator(
                        "Min 4 and max 35 character",
                        4, 35))
                .withValidator(new RegexpValidator(
                        "Patronymic has invalid characters",
                        REGEXP))
                .bind(Doctor::getPatronymic, Doctor::setPatronymic);
        binder.forField(specialization)
                .asRequired("!Empty field")
                .withValidator(new StringLengthValidator(
                        "Min 4 and max 35 character",
                        4, 35))
                .withValidator(new RegexpValidator(
                        "Specialization has invalid characters",
                        REGEXP))
                .bind(Doctor::getSpecialization, Doctor::setSpecialization);
    }

    private String getValidationErrorMessage(BinderValidationStatus<Doctor> binderValidationStatus) {
        return binderValidationStatus.getValidationErrors().get(0).getErrorMessage();
    }

}