package ru.bublig.testtask.service;

import ru.bublig.testtask.config.HSQLDBConnection;
import ru.bublig.testtask.model.Patient;
import ru.bublig.testtask.DAO.CrudDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientCrudDAO extends CrudDAO<Patient, Long> {

    private final Connection connection;

    public PatientCrudDAO(HSQLDBConnection connection) {
        this.connection = connection.getConnection();
    }

    @Override
    public List<Patient> getAll() {
        List<Patient> patientList = new ArrayList<>();
        String sql = "SELECT t.ID, t.FIRSTNAME, t.LASTNAME, t.PATRONYMIC, t.PHONE " +
                "FROM PUBLIC.PATIENT t";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Patient patient = new Patient(
                        resultSet.getLong("id"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("patronymic"),
                        resultSet.getString("phone")
                );
                patientList.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patientList;
    }

    @Override
    public Patient getEntityById(Long id) {
        ResultSet resultSet;
        Patient patient = new Patient();
        String sql = "SELECT t.ID, t.FIRSTNAME, t.LASTNAME, t.PATRONYMIC, t.PHONE " +
                "FROM PUBLIC.PATIENT t " +
                "WHERE ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                patient.setId(resultSet.getLong("id"));
                patient.setFirstName(resultSet.getString("firstName"));
                patient.setLastName(resultSet.getString("lastName"));
                patient.setPatronymic(resultSet.getString("patronymic"));
                patient.setPhone(resultSet.getString("phone"));
            } else {
                throw new IllegalArgumentException(Patient.class.getSimpleName() + "Error: getEntityById");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patient;
    }

    @Override
    public Patient update(Patient entity) {
        String sql = "UPDATE PUBLIC.PATIENT t " +
                "SET t.FIRSTNAME = ?, t.LASTNAME = ?, t.PATRONYMIC = ?, t.PHONE = ?" +
                "WHERE t.ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setString(3, entity.getPatronymic());
            preparedStatement.setString(4, entity.getPhone());
            preparedStatement.setLong(5, entity.getId());
            if (preparedStatement.executeUpdate() != 1) {
                throw new IllegalArgumentException(Patient.class.getSimpleName() + "Error: update");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM PUBLIC.PATIENT t " +
                "WHERE t.id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            if (preparedStatement.executeUpdate() != 1) {
                throw new IllegalArgumentException(Patient.class.getSimpleName() + "Error: delete");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean create(Patient entity) {
        String sql = "INSERT INTO PUBLIC.PATIENT (FIRSTNAME, LASTNAME, PATRONYMIC, PHONE) " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, entity.getFirstName());
            preparedStatement.setString(2, entity.getLastName());
            preparedStatement.setString(3, entity.getPatronymic());
            preparedStatement.setString(4, entity.getPhone());
            if (preparedStatement.executeUpdate() != 1) {
                throw new IllegalArgumentException(Patient.class.getSimpleName() + "Error: create");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean save(Patient entity) {
        return false;
    }

    @Override
    public List<Patient> getAll(String filter) {
        return null;
    }

    @Override
    protected List<Patient> getAllHelper(String sql) {
        return null;
    }
}
