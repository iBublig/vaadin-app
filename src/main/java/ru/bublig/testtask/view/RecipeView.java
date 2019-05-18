package ru.bublig.testtask.view;

import com.vaadin.annotations.Theme;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import ru.bublig.testtask.component.RecipeEditor;
import ru.bublig.testtask.config.HSQLDBConnection;
import ru.bublig.testtask.model.Recipe;
import ru.bublig.testtask.model.RecipeStatus;
import ru.bublig.testtask.service.RecipeService;

@Theme("mytheme")
public class RecipeView extends UI {
    //TODO Добавление нового рецепта, редактирование и удаление существующего
    //TODO Все формы ввода должны валидировать данные в соответствии с их типом и допустимыми значениями

    // Список рецептов с фильтром: таблица, кнопки "Добавить", "Изменить", "Удалить", панель фильтра с полями "Пациент",
    // "Приоритет", "Описание" и кнопкой "Применить"
    // Фильтр по описанию предполагает вывод рецептов, содержащих в описании введённый в фильтр текст

    private final RecipeService recipeService = new RecipeService(HSQLDBConnection.getInstance());
    private Grid<Recipe> recipeGrid = new Grid<>(Recipe.class);

    private final TextField filterText = new TextField();
    private final TextField patientFilter = new TextField();
    private final NativeSelect<RecipeStatus> statusFilter = new NativeSelect<>("Status");
    private final Button acceptFilter = new Button( "Filter");
    private final Button addNewBtn = new Button( "Add new recipe");
    private final RecipeEditor recipeEditor = new RecipeEditor( this);

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

        CssLayout filtering = new CssLayout();
        filtering.addComponents(filterText, patientFilter, statusFilter, acceptFilter, clearFilterTextBtn);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        acceptFilter.addClickListener(clickEvent -> updateListFilter());

        addNewBtn.addClickListener(clickEvent -> {
            recipeGrid.asSingleSelect().clear();
            recipeEditor.setRecipe(new Recipe());
        });

        HorizontalLayout toolbar = new HorizontalLayout(filtering, addNewBtn);
        HorizontalLayout main = new HorizontalLayout(recipeGrid, recipeEditor);

        recipeGrid.setColumns("id", "description", "patientId", "doctorId", "createData", "validity", "status");

        main.setSizeFull();
        recipeGrid.setSizeFull();

        main.setExpandRatio(recipeGrid, 1);

        layout.addComponents(toolbar, main);

        updateList();

        setContent(layout);

        recipeEditor.setVisible(false);

        recipeGrid.asSingleSelect().addValueChangeListener(valueChangeEvent -> {
            if (valueChangeEvent.getValue() == null) {
                recipeEditor.setVisible(false);
            } else
                recipeEditor.setRecipe(valueChangeEvent.getValue());
        });
    }

    public void updateList() {
        recipeGrid.setItems(recipeService.getAll());
    }

    public void updateListFilter() {
        recipeGrid.setItems(recipeService.getAll(filterText.getValue(), patientFilter.getValue(), statusFilter.getValue().name()));
    }
}
