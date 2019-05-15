package ru.bublig.testtask;

import ru.bublig.testtask.config.HSQLDBConnection;
import ru.bublig.testtask.model.Doctor;
import ru.bublig.testtask.service.DoctorService;

import java.sql.SQLException;
import java.util.List;

public class TestApp {

    public static void main(String[] args) throws SQLException {
//        PatientCrudDAO patientCrudDAO = new PatientCrudDAO(HSQLDBConnection.getInstance());
        DoctorService doctorService = new DoctorService(HSQLDBConnection.getInstance());

        List<Doctor> all = doctorService.getAll();
        for (Doctor doctor: all) {
            System.out.println(doctor);
        }

        Doctor testDoctor = doctorService.getEntityById(1L);
        Doctor doctor = new Doctor("Ilya", "Sirotin", "Vladimirovich", "Doc");
        testDoctor.setFirstName("Lera");

        doctorService.save(testDoctor);
        doctorService.save(doctor);


    }
}
