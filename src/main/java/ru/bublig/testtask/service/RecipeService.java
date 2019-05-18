package ru.bublig.testtask.service;

import ru.bublig.testtask.DAO.CrudDAO;
import ru.bublig.testtask.config.HSQLDBConnection;
import ru.bublig.testtask.model.Recipe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeService extends CrudDAO<Recipe, Long> {

    private final Connection connection;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public RecipeService(HSQLDBConnection connection) {
        this.connection = connection.getConnection();
        doctorService = new DoctorService(HSQLDBConnection.getInstance());
        patientService = new PatientService(HSQLDBConnection.getInstance());
    }

    @Override
    public List<Recipe> getAll() {
        String sql = "SELECT t.ID, t.DESCRIPTION, t.PATIENTID, t.DOCTORID, t.CREATEDATE, t.VALIDITY, t.STATUS " +
                "FROM PUBLIC.RECIPE t ";
        return getAllHelper(sql);
    }

    @Override
    public Recipe getEntityById(Long id) {
        ResultSet resultSet;
        Recipe recipe = new Recipe();
        String sql = "SELECT t.ID, t.DESCRIPTION, t.PATIENTID, t.DOCTORID, t.CREATEDATE, t.VALIDITY, t.STATUS " +
                "FROM PUBLIC.RECIPE t " +
                "WHERE ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                recipe.setId(resultSet.getLong("ID"));
                recipe.setDescription(resultSet.getString("DESCRIPTION").trim());
                recipe.setPatientId(resultSet.getLong("PATIENTID"));
                recipe.setDoctorId(resultSet.getLong("DOCTORID"));
                recipe.setCreateData(resultSet.getDate("CREATEDATE"));
                recipe.setValidity(resultSet.getDate("VALIDITY"));
                recipe.setStatus(resultSet.getString("STATUS").trim());
            } else {
                throw new IllegalArgumentException(Recipe.class.getSimpleName() + "Error: getEntityById");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipe;
    }

    @Override
    public void update(Recipe entity) {
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public void create(Recipe entity) {
    }

    @Override
    public List<Recipe> getAll(String filter) {
        return null;
    }

    @Override
    protected List<Recipe> getAllHelper(String sql) {
        List<Recipe> recipeList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Recipe recipe = new Recipe(
                        resultSet.getLong("ID"),
                        resultSet.getString("DESCRIPTION").trim(),
                        resultSet.getLong("PATIENTID"),
                        resultSet.getLong("DOCTORID"),
                        resultSet.getDate("CREATEDATE"),
                        resultSet.getDate("VALIDITY"),
                        resultSet.getString("STATUS").trim()
                );
                recipeList.add(recipe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipeList;
    }

    public List<Recipe> getAll(String textFilterValue, String patientFilterValue, String statusFilterValue) {
        String sql = "SELECT t.ID, t.DESCRIPTION, t.PATIENTID, t.DOCTORID, t.CREATEDATE, t.VALIDITY, t.STATUS " +
                "FROM PUBLIC.RECIPE t " +
                "WHERE concat(t.DESCRIPTION, concat(t.PATIENTID, t.STATUS)) " +
                "LIKE '%" + textFilterValue + "%" + patientFilterValue + "%" + statusFilterValue + "%'";
        return getAllHelper(sql);
    }
}
