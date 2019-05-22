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
                recipe.setPatient(patientService.getEntityById(resultSet.getLong("PATIENTID")));
                recipe.setDoctor(doctorService.getEntityById(resultSet.getLong("DOCTORID")));
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
        String sql = "UPDATE PUBLIC.RECIPE t " +
                "SET t.DESCRIPTION = ?, t.PATIENTID = ?, t.DOCTORID = ?, t.CREATEDATE = ?, t.VALIDITY = ?, t.STATUS = ?" +
                "WHERE t.ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setPreparedStatement(entity, preparedStatement);
            preparedStatement.setLong(7, entity.getId());
            if (preparedStatement.executeUpdate() != 1) {
                throw new IllegalArgumentException(Recipe.class.getSimpleName() + "Error: update");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM PUBLIC.RECIPE t " +
                "WHERE t.id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            if (preparedStatement.executeUpdate() != 1) {
                throw new IllegalArgumentException(Recipe.class.getSimpleName() + "Error: delete");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void create(Recipe entity) {
        String sql = "INSERT INTO PUBLIC.RECIPE (DESCRIPTION, PATIENTID, DOCTORID, CREATEDATE, VALIDITY, STATUS) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setPreparedStatement(entity, preparedStatement);
            if (preparedStatement.executeUpdate() != 1) {
                throw new IllegalArgumentException(Recipe.class.getSimpleName() + "Error: create");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Recipe> getAll(String filter) {
        String sql = "SELECT t.ID, t.DESCRIPTION, t.PATIENTID, t.DOCTORID, t.CREATEDATE, t.VALIDITY, t.STATUS " +
                "FROM PUBLIC.RECIPE t " +
                "WHERE t.DESCRIPTION " +
                "LIKE '%" + filter + "%'";
        return getAllHelper(sql);
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
                        patientService.getEntityById(resultSet.getLong("PATIENTID")),
                        doctorService.getEntityById(resultSet.getLong("DOCTORID")),
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
        String sql = "SELECT t.ID, t.DESCRIPTION, t.PATIENTID, t.DOCTORID, t.CREATEDATE, t.VALIDITY, t.STATUS, " +
                "p.FIRSTNAME, p.LASTNAME, p.PATRONYMIC " +
                "FROM PUBLIC.RECIPE t, PUBLIC.PATIENT p " +
                "WHERE t.PATIENTID = p.ID " +
                "AND CONCAT(t.DESCRIPTION, CONCAT(CONCAT(p.LASTNAME, CONCAT(p.FIRSTNAME, p.PATRONYMIC)), t.STATUS)) " +
                "LIKE '%" + textFilterValue + "%" + patientFilterValue + "%" + statusFilterValue + "%'";
        return getAllHelper(sql);
    }

    public List<String> getStatistic(){
        String sql = "SELECT d.FIRSTNAME, d.PATRONYMIC, COUNT(t.DOCTORID) " +
                "FROM PUBLIC.RECIPE t, PUBLIC.DOCTOR d " +
                "WHERE t.DOCTORID = d.ID " +
                "GROUP BY d.FIRSTNAME, d.PATRONYMIC";
        List<String> result = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                result.add(
                        resultSet.getString(1).trim() + " " +
                        resultSet.getString(2).trim() + ": " +
                        resultSet.getString(3).trim()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void setPreparedStatement(Recipe entity, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, entity.getDescription());
        preparedStatement.setLong(2, entity.getPatient().getId());
        preparedStatement.setLong(3, entity.getDoctor().getId());
        preparedStatement.setDate(4, new Date(entity.getCreateData().getTime()));
        preparedStatement.setDate(5, new Date(entity.getValidity().getTime()));
        preparedStatement.setString(6, entity.getStatus().toString());
    }
}
