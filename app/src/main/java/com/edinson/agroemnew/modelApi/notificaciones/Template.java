package com.edinson.agroemnew.modelApi.notificaciones;

public class Template {
    private String title;
    private String description;

    // Default constructor
    public Template() {
    }

    // Constructor with parameters
    public Template(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Getter and Setter for title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // Getter and Setter for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}