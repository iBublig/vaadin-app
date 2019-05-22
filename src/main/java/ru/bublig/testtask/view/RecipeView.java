package ru.bublig.testtask.view;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.bublig.testtask.component.RecipeEditor;
import ru.bublig.testtask.component.RecipeWindowEditor;
import ru.bublig.testtask.config.HSQLDBConnection;
import ru.bublig.testtask.model.Doctor;
import ru.bublig.testtask.model.Patient;
import ru.bublig.testtask.model.Recipe;
import ru.bublig.testtask.model.RecipeStatus;
import ru.bublig.testtask.service.RecipeService;

import java.util.Date;

@Theme("mytheme")
public class RecipeView extends UI {
    private final RecipeService recipeService = new RecipeService(HSQLDBConnection.getInstance());
    private Grid<Recipe> recipeGrid = new Grid<>(Recipe.class);

    private final TextField filterText = new TextField();
    private final TextField patientFilter = new TextField();
    private final NativeSelect<RecipeStatus> statusFilter = new NativeSelect<>();
    private final Button acceptFilter = new Button("Filter");
    private final Button addNewBtn = new Button("Add new recipe");
    private final RecipeWindowEditor recipeEditor = new RecipeWindowEditor(this);

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final VerticalLayout layout = new VerticalLayout();

        Button clearFilterTextBtn = new Button(VaadinIcons.CLOSE);
        clearFilterTextBtn.setDescription("Clear the current input");
        clearFilterTextBtn.addClickListener(clickEvent -> {
            patientFilter.clear();
            filterText.clear();
            statusFilter.setSelectedItem(RecipeStatus.Normal);
            updateList();
        });

        filterText.setPlaceholder("Filter by description");
        patientFilter.setPlaceholder("patient");
        statusFilter.setItems(RecipeStatus.values());
        statusFilter.setSelectedItem(RecipeStatus.Normal);
        statusFilter.setEmptySelectionAllowed(false);

        CssLayout filtering = new CssLayout();
        filtering.addComponents(filterText, patientFilter, statusFilter, acceptFilter, clearFilterTextBtn);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        acceptFilter.addClickListener(clickEvent -> updateListFilter());

        addNewBtn.addClickListener(clickEvent -> {
            recipeGrid.asSingleSelect().clear();
            recipeEditor.setRecipe(new Recipe("", new Patient(), new Doctor(), new Date(), new Date(), RecipeStatus.Normal));
            UI.getCurrent().addWindow(recipeEditor);
        });

        HorizontalLayout toolbar = new HorizontalLayout(filtering, addNewBtn);
        HorizontalLayout main = new HorizontalLayout(recipeGrid);

        recipeGrid.setColumns("id", "description", "patient", "doctor", "createData", "validity", "status");

        main.setSizeFull();
        recipeGrid.setSizeFull();

        main.setExpandRatio(recipeGrid, 1);

        layout.addComponents(toolbar, main);

        updateList();

        setContent(layout);

        recipeGrid.asSingleSelect().addValueChangeListener(valueChangeEvent -> {
            if (!(valueChangeEvent.getValue() == null)) {
                recipeEditor.setRecipe(valueChangeEvent.getValue());
                UI.getCurrent().addWindow(recipeEditor);
            }
        });
    }

    public void updateList() {
        deselectAll();
        recipeGrid.setItems(recipeService.getAll());
    }

    public void deselectAll() {
        recipeGrid.deselectAll();
    }

    public void updateListFilter() {
        recipeGrid.setItems(recipeService.getAll(filterText.getValue(), patientFilter.getValue(), statusFilter.getValue().name()));
    }
}
