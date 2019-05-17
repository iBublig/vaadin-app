package ru.bublig.testtask.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

public class Recipe {
    private Long id;
    private String description;
    private Long patientId;
    private Long doctorId;
    private Date createData;
    private Date validity;
    private RecipeStatus status;

    public Recipe() {
    }

    public Recipe(Long id, String description, Long patientId, Long doctorId, Date createData, Date validity, RecipeStatus status) {
        this.id = id;
        this.description = description;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.createData = createData;
        this.validity = validity;
        this.status = status;
    }

    public Recipe(Long id, String description, Long patientId, Long doctorId, Date createData, Date validity, String status) {
        this.id = id;
        this.description = description;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.createData = createData;
        this.validity = validity;
        this.status = RecipeStatus.getRecipeStatusByText(status);
    }

    public Recipe(String description, Long patientId, Long doctorId, Date createData, Date validity, RecipeStatus status) {
        this.description = description;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.createData = createData;
        this.validity = validity;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Objects.equals(id, recipe.id) &&
                Objects.equals(description, recipe.description) &&
                Objects.equals(patientId, recipe.patientId) &&
                Objects.equals(doctorId, recipe.doctorId) &&
                Objects.equals(createData, recipe.createData) &&
                Objects.equals(validity, recipe.validity) &&
                status == recipe.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, patientId, doctorId, createData, validity, status);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", patientId=" + patientId +
                ", doctorId=" + doctorId +
                ", createData=" + createData +
                ", validity=" + validity +
                ", status=" + status +
                '}';
    }
}
