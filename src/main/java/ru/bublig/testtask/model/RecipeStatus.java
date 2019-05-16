package ru.bublig.testtask.model;

public enum RecipeStatus {
    Normal("Normal"),
    Cito("Cito"),
    Statim("Statim");

    private final String text;

    RecipeStatus(String text) {
        this.text = text;
    }

    public static RecipeStatus getRecipeStatusByText(String text) {
        for (RecipeStatus recipeStatus: RecipeStatus.values()){
            if (recipeStatus.text.equals(text))
                return recipeStatus;
        }
        return null;
    }

    @Override
    public String toString() {
        return text;
    }
}
