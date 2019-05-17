package ru.bublig.testtask.component;

import com.vaadin.ui.FormLayout;
import ru.bublig.testtask.model.Recipe;
import ru.bublig.testtask.view.RecipeView;

public class RecipeEditor extends FormLayout {

    private RecipeView recipeView;

    public RecipeEditor(RecipeView recipeView) {
        this.recipeView = recipeView;
    }

    public void setRecipe(Recipe value) {
    }
}
