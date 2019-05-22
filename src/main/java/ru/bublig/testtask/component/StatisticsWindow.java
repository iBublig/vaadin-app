package ru.bublig.testtask.component;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import ru.bublig.testtask.config.HSQLDBConnection;
import ru.bublig.testtask.service.RecipeService;

public class StatisticsWindow extends Window {

    private final RecipeService recipeService;

    public StatisticsWindow() {
        super("Статистика по выписанным рецептам");
        recipeService = new RecipeService(HSQLDBConnection.getInstance());

        initUi();
    }

    private void initUi() {
        center();
        setResizable(false);
        setModal(true);
        setWidth("350px");
        VerticalLayout statistic = new VerticalLayout();
        statistic.setDefaultComponentAlignment(Alignment.BOTTOM_CENTER);
        for (String string: recipeService.getStatistic()) {
            statistic.addComponent(new Label(string));
        }
        setContent(statistic);
    }
}
