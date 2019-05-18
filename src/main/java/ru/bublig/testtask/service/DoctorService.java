package ru.bublig.testtask.service;

import ru.bublig.testtask.config.HSQLDBConnection;
import ru.bublig.testtask.model.Doctor;
import ru.bublig.testtask.DAO.CrudDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorService extends CrudDAO<Doctor, Long> {

    private final Connection connection;

    public DoctorService(HSQLDBConnection connection) {
        this.connection = connection.getConnection();
    }

    @Override
    public List<Doctor> getAll() {
        String sql = "SELECT t.ID, t.FIRSTNAME, t.LASTNAME, t.PATRONYMIC, t.SPECIALIZATION " +
                "FROM PUBLIC.DOCTOR t";
        return getAllHelper(sql);
    }

    @Override
    public Doctor getEntityById(Long id) {
        ResultSet resultSet;
        Doctor doctor = new Doctor();
        String sql = "SELECT t.ID, t.FIRSTNAME, t.LASTNAME, t.PATRONYMIC, t.SPECIALIZATION " +
                "FROM PUBLIC.DOCTOR t " +
                "WHERE ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                doctor.setId(resultSet.getLong("ID"));
                doctor.setFirstName(resultSet.getString("FIRSTNAME").trim());
                doctor.setLastName(resultSet.getString("LASTNAME").trim());
                doctor.setPatronymic(resultSet.getString("PATRONYMIC").trim());
                doctor.setSpecialization(resultSet.getString("SPECIALIZATION").trim());
            } else {
                throw new IllegalArgumentException(Doctor.class.getSimpleName() + "Error: getEntityById");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctor;
    }

    @Override
    protected void update(Doctor entity) {
        String sql = "UPDATE PUBLIC.DOCTOR t " +
                "SET t.FIRSTNAME = ?, t.LASTNAME = ?, t.PATRONYMIC = ?, t.SPECIALIZATION = ?" +
                "WHERE t.ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setPreparedStatement(entity, preparedStatement);
            preparedStatement.setLong(5, entity.getId());
            if (preparedStatement.executeUpdate() != 1) {
                throw new IllegalArgumentException(Doctor.class.getSimpleName() + "Error: update");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM PUBLIC.DOCTOR t " +
                "WHERE t.id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            if (preparedStatement.executeUpdate() != 1) {
                throw new IllegalArgumentException(Doctor.class.getSimpleName() + "Error: delete");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void create(Doctor entity) {
        String sql = "INSERT INTO PUBLIC.DOCTOR (FIRSTNAME, LASTNAME, PATRONYMIC, SPECIALIZATION) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            setPreparedStatement(entity, preparedStatement);
            if (preparedStatement.executeUpdate() != 1) {
                throw new IllegalArgumentException(Doctor.class.getSimpleName() + "Error: create");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Doctor> getAll(String filter) {
        String sql = "SELECT t.ID, t.FIRSTNAME, t.LASTNAME, t.PATRONYMIC, t.SPECIALIZATION " +
                "FROM PUBLIC.DOCTOR t " +
                "WHERE concat(t.FIRSTNAME, concat(t.LASTNAME, t.PATRONYMIC)) " +
                "LIKE '%" + filter + "%'";
        return getAllHelper(sql);
    }

    @Override
    protected List<Doctor> getAllHelper(String sql) {
        List<Doctor> doctorList = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Doctor doctor = new Doctor(
                        resultSet.getLong("ID"),
                        resultSet.getString("FIRSTNAME").trim(),
                        resultSet.getString("LASTNAME").trim(),
                        resultSet.getString("PATRONYMIC").trim(),
                        resultSet.getString("SPECIALIZATION").trim()
                );
                doctorList.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctorList;
    }

    private void setPreparedStatement(Doctor entity, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, entity.getFirstName());
        preparedStatement.setString(2, entity.getLastName());
        preparedStatement.setString(3, entity.getPatronymic());
        preparedStatement.setString(4, entity.getSpecialization());
    }
}
