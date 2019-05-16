package ru.bublig.testtask;

import ru.bublig.testtask.config.HSQLDBConnection;
import ru.bublig.testtask.model.Recipe;
import ru.bublig.testtask.service.RecipeService;

import java.util.Date;

public class TestApp {

    public static void main(String[] args) {
//        PatientService patientCrudDAO = new PatientService(HSQLDBConnection.getInstance());
//        DoctorService doctorService = new DoctorService(HSQLDBConnection.getInstance());
//
//        List<Doctor> all = doctorService.getAll();
//        for (Doctor doctor: all) {
//            System.out.println(doctor);
//        }
//
//        Doctor testDoctor = doctorService.getEntityById(5L);
//        Doctor doctor = new Doctor("Ilya", "Sirotin", "Vladimirovich", "Doctor");
//        testDoctor.setFirstName("Kolya");
//
//        doctorService.save(testDoctor);
//        doctorService.save(doctor);

        RecipeService recipeService = new RecipeService(HSQLDBConnection.getInstance());

        Recipe recipe = recipeService.getEntityById(1L);

        Date date = new Date();
        System.out.println(date);

    }
}
