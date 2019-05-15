package ru.bublig.testtask.service;

import ru.bublig.testtask.DAO.CrudDAO;
import ru.bublig.testtask.model.Recipe;

import java.util.List;

public class RecipeService extends CrudDAO<Recipe, Long> {
    @Override
    public List<Recipe> getAll() {
        return null;
    }

    @Override
    public Recipe getEntityById(Long id) {
        return null;
    }

    @Override
    public Recipe update(Recipe entity) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public boolean create(Recipe entity) {
        return false;
    }

    @Override
    public boolean save(Recipe entity) {
        return false;
    }

    @Override
    public List<Recipe> getAll(String filter) {
        return null;
    }

    @Override
    protected List<Recipe> getAllHelper(String sql) {
        return null;
    }
}
