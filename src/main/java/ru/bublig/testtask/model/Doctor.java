package ru.bublig.testtask.model;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("serial")
public class Doctor extends Entity implements Serializable {
    private String firstName;
    private String lastName;
    private String patronymic;
    private String specialization;

    public Doctor() {
    }

    public Doctor(Long id, String firstName, String lastName, String patronymic, String specialization) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.specialization = specialization;
    }

    public Doctor(String firstName, String lastName, String patronymic, String specialization) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.specialization = specialization;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public boolean isPersisted() {
        return getId() != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(id, doctor.id) &&
                Objects.equals(firstName, doctor.firstName) &&
                Objects.equals(lastName, doctor.lastName) &&
                Objects.equals(patronymic, doctor.patronymic) &&
                Objects.equals(specialization, doctor.specialization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, patronymic, specialization);
    }

    @Override
    public String toString() {
        if (lastName == null || firstName == null || patronymic == null)
            return "";
        else
            return lastName + " " + firstName + " " + patronymic;
    }
}
