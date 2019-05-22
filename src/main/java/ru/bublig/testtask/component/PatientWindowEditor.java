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
import ru.bublig.testtask.model.Patient;
import ru.bublig.testtask.service.PatientService;
import ru.bublig.testtask.view.PatientView;

public class PatientWindowEditor extends Window {
    private static final String REGEXP = "^[а-яА-ЯёЁa-zA-Z ]+$";
    private static final String PHONE = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$";

    private final PatientService patientService;

    private Patient patient;

    private TextField firstName = new TextField("First name");
    private TextField lastName = new TextField("Last name");
    private TextField patronymic = new TextField("Patronymic");
    private TextField phone = new TextField("Phone");

    private Label error = new Label();

    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete");

    private Binder<Patient> binder = new Binder<>(Patient.class);

    private PatientView patientView;

    public PatientWindowEditor(PatientView patientView) {
        super("Patient");
        this.patientView = patientView;
        patientService = new PatientService(HSQLDBConnection.getInstance());

        initUi();
    }

    private void initUi() {
        center();
        setResizable(false);
        setModal(true);
        setWidth("300px");
        error.setVisible(false);

        phone.setPlaceholder("+7(123)456-78-90");

        HorizontalLayout buttons = new HorizontalLayout(save, cancel, delete);
        VerticalLayout window = new VerticalLayout(lastName, firstName, patronymic, phone, buttons, error);
        setCenter(buttons, window);

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        binder.setValidationStatusHandler((BinderValidationStatusHandler<Patient>) binderValidationStatus -> {
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
            BinderValidationStatus<Patient> status = binder.validate();
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

    public void setPatient(Patient patient) {
        this.patient = patient;
        binder.setBean(patient);
        error.setVisible(false);

        delete.setVisible(patient.isPersisted());
        lastName.selectAll();
    }

    private void delete() {
        if (!patientService.delete(patient.getId())) {
            error.setValue("Error: patient in recipe");
            error.setVisible(true);
            return;
        }
        close();
    }

    private void setFocus(BinderValidationStatus<Patient> binderValidationStatus) {
        HasValue field = binderValidationStatus.getFieldValidationErrors().get(0).getField();
        ((TextField) field).focus();
    }

    private void save() {
        patientService.save(patient);
        close();
    }

    private void setCenter(HorizontalLayout buttons, VerticalLayout window) {
        window.setComponentAlignment(lastName, Alignment.BOTTOM_CENTER);
        window.setComponentAlignment(firstName, Alignment.BOTTOM_CENTER);
        window.setComponentAlignment(patronymic, Alignment.BOTTOM_CENTER);
        window.setComponentAlignment(phone, Alignment.BOTTOM_CENTER);
        window.setComponentAlignment(buttons, Alignment.BOTTOM_CENTER);
    }

    @Override
    public void close() {
        super.close();
        patientView.updateList();
        patientView.deselectAll();
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
                .bind(Patient::getFirstName, Patient::setFirstName);
        binder.forField(lastName)
                .asRequired("!Empty field")
                .withValidator(new StringLengthValidator(
                        "Min 4 and max 35 character",
                        4, 35))
                .withValidator(new RegexpValidator(
                        "Last name has invalid characters",
                        REGEXP))
                .bind(Patient::getLastName, Patient::setLastName);
        binder.forField(patronymic)
                .asRequired("!Empty field")
                .withValidator(new StringLengthValidator(
                        "Min 4 and max 35 character",
                        4, 35))
                .withValidator(new RegexpValidator(
                        "Patronymic has invalid characters",
                        REGEXP))
                .bind(Patient::getPatronymic, Patient::setPatronymic);
        binder.forField(phone)
                .asRequired("!Empty field")
                .withValidator(new StringLengthValidator(
                        "Min 4 and max 35 character",
                        4, 35))
                .withValidator(new RegexpValidator(
                        "Phone has invalid characters",
                        PHONE))
                .bind(Patient::getPhone, Patient::setPhone);
    }

    private String getValidationErrorMessage(BinderValidationStatus<Patient> binderValidationStatus) {
        return binderValidationStatus.getValidationErrors().get(0).getErrorMessage();
    }
}
