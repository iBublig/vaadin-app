package ru.bublig.testtask.component;

import com.vaadin.data.*;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Setter;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.bublig.testtask.config.HSQLDBConnection;
import ru.bublig.testtask.model.Doctor;
import ru.bublig.testtask.model.Patient;
import ru.bublig.testtask.model.Recipe;
import ru.bublig.testtask.model.RecipeStatus;
import ru.bublig.testtask.service.DoctorService;
import ru.bublig.testtask.service.PatientService;
import ru.bublig.testtask.service.RecipeService;
import ru.bublig.testtask.view.RecipeView;

import java.time.ZoneId;

public class RecipeWindowEditor extends Window {
    private final RecipeService recipeService;
    private final PatientService patientService;
    private final DoctorService doctorService;

    private Recipe recipe;

    private final TextField description = new TextField("Description");
    private final TextField patient = new TextField("Patient");
    private final TextField doctor = new TextField("Doctor");
    private final DateField createData = new DateField("Create Data");
    private final DateField validity = new DateField("Validity");
    private final NativeSelect<RecipeStatus> recipeStatus = new NativeSelect<>("Status");

    private Label error = new Label();

    private final NativeSelect<Patient> patientNativeSelect = new NativeSelect<>();
    private final NativeSelect<Doctor> doctorNativeSelect = new NativeSelect<>();

    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete");

    private Binder<Recipe> binder = new Binder<>(Recipe.class);

    private RecipeView recipeView;

    public RecipeWindowEditor(RecipeView recipeView) {
        this.recipeView = recipeView;
        recipeService = new RecipeService(HSQLDBConnection.getInstance());
        patientService = new PatientService(HSQLDBConnection.getInstance());
        doctorService = new DoctorService(HSQLDBConnection.getInstance());

        initUi();
    }

    private void initUi() {
        initNativeSelect();
        center();
        setResizable(false);
        setModal(true);
        setWidth("300px");
        error.setVisible(false);

        HorizontalLayout buttons = new HorizontalLayout(save, cancel, delete);
        VerticalLayout content = new VerticalLayout(
                description, patient, patientNativeSelect,
                doctor, doctorNativeSelect, createData,
                validity, recipeStatus, buttons, error
        );

        recipeStatus.setItems(RecipeStatus.values());
        recipeStatus.setEmptySelectionAllowed(false);
        createData.setDateFormat("yyyy-MM-dd");
        validity.setDateFormat("yyyy-MM-dd");
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        initBinder();

        save.addClickListener(e -> {
            BinderValidationStatus<Recipe> status = binder.validate();
            if (status.isOk()) {
                if (patientNativeSelect.getValue() == null || doctorNativeSelect.getValue() == null) {
                    error.setValue("Вы не выбрали доктора или пациента");
                    error.setVisible(true);
                    return;
                }
                error.setVisible(false);
                save();
            } else {
                error.setVisible(true);
                error.setValue(getValidationErrorMessage(status));
            }
        });
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> close());
        patient.addValueChangeListener(e -> {
            if (e.isUserOriginated()) {
                patientNativeSelect.clear();
                patientNativeSelect.setItems(patientService.getAll(patient.getValue()));
                patientNativeSelect.setSizeFull();
            }
        });
        patientNativeSelect.addValueChangeListener(e -> {
            if (patientNativeSelect.getValue() != null) {
                patient.setValue(patientNativeSelect.getValue().toString());
                recipe.setPatient(patientNativeSelect.getValue());
            }
        });
        doctor.addValueChangeListener(e -> {
            if (e.isUserOriginated()) {
                doctorNativeSelect.clear();
                doctorNativeSelect.setItems(doctorService.getAll(doctor.getValue()));
                doctorNativeSelect.setSizeFull();
            }
        });
        doctorNativeSelect.addValueChangeListener(e -> {
            if (doctorNativeSelect.getValue() != null) {
                doctor.setValue(doctorNativeSelect.getValue().toString());
                recipe.setDoctor(doctorNativeSelect.getValue());
            }
        });

        setContent(content);
    }

    @Override
    public void close() {
        super.close();
        recipeView.updateList();
        recipeView.deselectAll();
    }

    private void setFocus(BinderValidationStatus<Recipe> binderValidationStatus) {
        HasValue field = binderValidationStatus.getFieldValidationErrors().get(0).getField();
        ((TextField) field).focus();
    }

    private String getValidationErrorMessage(BinderValidationStatus<Recipe> binderValidationStatus) {
        return binderValidationStatus.getValidationErrors().get(0).getErrorMessage();
    }

    private void initBinder() {
        binder.setValidationStatusHandler((BinderValidationStatusHandler<Recipe>) binderValidationStatus -> {
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
        binder.forField(description)
                .withValidator(new StringLengthValidator(
                        "Min 10 and max 30 character",
                        10, 30))
                .bind(Recipe::getDescription, Recipe::setDescription);
        binder.forField(createData)
                .withConverter(new LocalDateToDateConverter(ZoneId.systemDefault()))
                .bind(Recipe::getCreateData, Recipe::setCreateData);
        binder.forField(validity)
                .withConverter(new LocalDateToDateConverter(ZoneId.systemDefault()))
                .bind(Recipe::getValidity, Recipe::setValidity);
        binder.forField(recipeStatus)
                .bind(Recipe::getStatus, Recipe::setStatus);

        binder.forField(patient)
                .withValidator(new StringLengthValidator(
                        "max 30 character",
                        0, 30))
                .bind(
                        (ValueProvider<Recipe, String>) recipe -> recipe.getPatient().toString(),
                        (Setter<Recipe, String>) (recipe, s) -> patient.setValue(s));

        binder.forField(doctor)
                .withValidator(new StringLengthValidator(
                        "max 30 character",
                        0, 30))
                .bind(
                        (ValueProvider<Recipe, String>) recipe -> recipe.getDoctor().toString(),
                        (Setter<Recipe, String>) (recipe, s) -> doctor.setValue(s));

        binder.bindInstanceFields(this);
    }

    private void updateField() {
        doctorNativeSelect.clear();
        patientNativeSelect.clear();
        description.clear();
        createData.clear();
        validity.clear();
        recipeStatus.clear();
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        updateField();
        binder.setBean(recipe);

        patientNativeSelect.setItems(patientService.getAll());
        doctorNativeSelect.setItems(doctorService.getAll());
        if (recipe.isPersisted()) {
            patientNativeSelect.setSelectedItem(recipe.getPatient());
            doctorNativeSelect.setSelectedItem(recipe.getDoctor());
        }

        recipeStatus.setSelectedItem(recipe.getStatus() == null ? RecipeStatus.Normal : recipe.getStatus());
        delete.setVisible(recipe.isPersisted());
        description.selectAll();
    }

    private void delete() {
        recipeService.delete(recipe.getId());
        close();
    }

    private void save() {
        recipeService.save(recipe);
        close();
    }

    private void initNativeSelect() {
        doctorNativeSelect.setVisibleItemCount(5);
        doctorNativeSelect.setEmptySelectionAllowed(false);
        patientNativeSelect.setVisibleItemCount(5);
        patientNativeSelect.setEmptySelectionAllowed(false);
    }
}
