package ru.bublig.testtask.component;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.LocalDateToDateConverter;
import com.vaadin.data.converter.StringToLongConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.bublig.testtask.config.HSQLDBConnection;
import ru.bublig.testtask.model.Recipe;
import ru.bublig.testtask.model.RecipeStatus;
import ru.bublig.testtask.service.RecipeService;
import ru.bublig.testtask.view.RecipeView;

import java.time.ZoneId;

public class RecipeEditor extends FormLayout {
    private final RecipeService recipeService;

    private Recipe recipe;

    private final TextField description = new TextField("Description");
    private final TextField patient = new TextField("Patient");
    private final TextField doctor = new TextField("Doctor");
    private final DateField createData = new DateField("Create Data");
    private final DateField validity = new DateField("Validity");
    private final NativeSelect<RecipeStatus> recipeStatus = new NativeSelect<>("Status");


    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete");

    private Binder<Recipe> binder = new Binder<>(Recipe.class);

    private RecipeView recipeView;

    public RecipeEditor(RecipeView recipeView) {
        this.recipeView = recipeView;
        recipeService = new RecipeService(HSQLDBConnection.getInstance());


        setSizeUndefined();
        HorizontalLayout buttons = new HorizontalLayout(save, cancel, delete);
        addComponents(description, patient, doctor, createData, validity, recipeStatus, buttons);


        recipeStatus.setItems(RecipeStatus.values());
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

        binder.bindInstanceFields(this);

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        binder.setBean(recipe);

        recipeStatus.setSelectedItem(recipe.getStatus() == null ? RecipeStatus.Normal : recipe.getStatus());

        delete.setVisible(recipe.isPersisted());
        setVisible(true);
        description.selectAll();
    }

    private void delete() {
        recipeService.delete(recipe.getId());
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
}
