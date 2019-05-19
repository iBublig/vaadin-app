package ru.bublig.testtask.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Recipe extends Entity implements Serializable {
    private String description;
    private Patient patient;
    private Doctor doctor;
    private Date createData;
    private Date validity;
    private RecipeStatus status;

    public Recipe() {
    }

    public Recipe(Long id, String description, Patient patient, Doctor doctor, Date createData, Date validity, RecipeStatus status) {
        this.id = id;
        this.description = description;
        this.patient = patient;
        this.doctor = doctor;
        this.createData = createData;
        this.validity = validity;
        this.status = status;
    }

    public Recipe(Long id, String description, Patient patient, Doctor doctor, Date createData, Date validity, String status) {
        this.id = id;
        this.description = description;
        this.patient = patient;
        this.doctor = doctor;
        this.createData = createData;
        this.validity = validity;
        this.status = RecipeStatus.getRecipeStatusByText(status);
    }

    public Recipe(String description, Patient patient, Doctor doctor, Date createData, Date validity, RecipeStatus status) {
        this.description = description;
        this.patient = patient;
        this.doctor = doctor;
        this.createData = createData;
        this.validity = validity;
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Date getCreateData() {
        return createData;
    }

    public void setCreateData(Date createData) {
        this.createData = createData;
    }

    public Date getValidity() {
        return validity;
    }

    public void setValidity(Date validity) {
        this.validity = validity;
    }

    public RecipeStatus getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = RecipeStatus.getRecipeStatusByText(status);
    }

    public void setStatus(RecipeStatus status) {
        this.status = status;
    }

    public boolean isPersisted() {
        return getId() != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(id, recipe.id) &&
                Objects.equals(description, recipe.description) &&
                Objects.equals(patient, recipe.patient) &&
                Objects.equals(doctor, recipe.doctor) &&
                Objects.equals(createData, recipe.createData) &&
                Objects.equals(validity, recipe.validity) &&
                status == recipe.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, patient, doctor, createData, validity, status);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", patient=" + patient +
                ", doctor=" + doctor +
                ", createData=" + createData +
                ", validity=" + validity +
                ", status=" + status +
                '}';
    }
}
