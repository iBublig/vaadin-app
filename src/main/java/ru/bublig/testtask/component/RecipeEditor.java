package ru.bublig.testtask.component;

import com.vaadin.data.Binder;
import com.vaadin.data.ValueProvider;
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

public class RecipeEditor extends FormLayout {
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

    private final NativeSelect<Patient> patientNativeSelect = new NativeSelect<>();
    private final NativeSelect<Doctor> doctorNativeSelect = new NativeSelect<>();

    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete");

    private Binder<Recipe> binder = new Binder<>(Recipe.class);

    private RecipeView recipeView;

    public RecipeEditor(RecipeView recipeView) {
        this.recipeView = recipeView;
        recipeService = new RecipeService(HSQLDBConnection.getInstance());
        patientService = new PatientService(HSQLDBConnection.getInstance());
        doctorService = new DoctorService(HSQLDBConnection.getInstance());

        doctorNativeSelect.setVisible(false);
        doctorNativeSelect.setVisibleItemCount(5);
        doctorNativeSelect.setEmptySelectionAllowed(false);
        patientNativeSelect.setVisible(false);
        patientNativeSelect.setVisibleItemCount(5);
        patientNativeSelect.setEmptySelectionAllowed(false);

        setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout(save, cancel, delete);
        addComponents(
                description, patient, patientNativeSelect,
                doctor, doctorNativeSelect, createData,
                validity, recipeStatus, buttons
        );

        recipeStatus.setItems(RecipeStatus.values());
        recipeStatus.setEmptySelectionAllowed(false);
        createData.setDateFormat("yyyy-MM-dd");
        validity.setDateFormat("yyyy-MM-dd");

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        binder.forField(createData)
                .withConverter(new LocalDateToDateConverter(ZoneId.systemDefault()))
                .bind(Recipe::getCreateData, Recipe::setCreateData);
        binder.forField(validity)
                .withConverter(new LocalDateToDateConverter(ZoneId.systemDefault()))
                .bind(Recipe::getValidity, Recipe::setValidity);

        binder.forField(patient)
                .withValidator(new StringLengthValidator(
                "Min 3 and max 30 character",
                3, 30))
                .bind(
                (ValueProvider<Recipe, String>) recipe -> {
                    if (!recipe.getPatient().isPersisted())
                        return "";
                    else
                        return recipe.getPatient().toString();
                },
                (Setter<Recipe, String>) (recipe, s) -> patient.setValue(s));

        binder.forField(doctor)
                .withValidator(new StringLengthValidator(
                        "Min 3 and max 30 character",
                        3, 30))
                .bind(
                (ValueProvider<Recipe, String>) recipe -> {
                    if (!recipe.getDoctor().isPersisted())
                        return "";
                    else
                        return recipe.getDoctor().toString();
                },
                (Setter<Recipe, String>) (recipe, s) -> doctor.setValue(s));

        binder.bindInstanceFields(this);

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> setVisible(false));
        patient.addValueChangeListener(e -> {
            if (e.isUserOriginated()) {
                patientNativeSelect.setVisible(true);
                patientNativeSelect.clear();
                patientNativeSelect.setItems(patientService.getAll(patient.getValue()));
                patientNativeSelect.setSizeFull();
            }
        });
        patientNativeSelect.addValueChangeListener(e -> {
            patient.setValue(patientNativeSelect.getValue().toString());
            recipe.setPatient(patientNativeSelect.getValue());
        });
        doctor.addValueChangeListener(e -> {
            if (e.isUserOriginated()) {
                doctorNativeSelect.setVisible(true);
                doctorNativeSelect.clear();
                doctorNativeSelect.setItems(doctorService.getAll(doctor.getValue()));
                doctorNativeSelect.setSizeFull();
            }
        });
        doctorNativeSelect.addValueChangeListener(e -> {
            doctor.setValue(doctorNativeSelect.getValue().toString());
            recipe.setDoctor(doctorNativeSelect.getValue());
        });
    }

    public void setRecipe(Recipe recipe) {
        updateField();
        this.recipe = recipe;
        binder.setBean(recipe);

        recipeStatus.setSelectedItem(recipe.getStatus() == null ? RecipeStatus.Normal : recipe.getStatus());

        delete.setVisible(recipe.isPersisted());
        setVisible(true);
        description.selectAll();
    }

    private void delete() {
        if (!recipeService.delete(recipe.getId())) {
            addComponent(new Label("Ошибка при удалении"));
        }
        //TODO удаление с фильтром
        recipeView.updateList();
        setVisible(false);
    }

    private void save() {
        recipeService.save(recipe);
        //TODO обновление с фильтром
        recipeView.updateList();
        setVisible(false);
    }

    private void updateField() {
        doctorNativeSelect.setVisible(false);
        doctorNativeSelect.clear();
        patientNativeSelect.setVisible(false);
        patientNativeSelect.clear();
        description.clear();
        createData.clear();
        validity.clear();
        recipeStatus.clear();
    }
}
