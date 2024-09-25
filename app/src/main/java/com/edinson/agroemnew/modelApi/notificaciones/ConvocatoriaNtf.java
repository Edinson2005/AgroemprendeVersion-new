package com.edinson.agroemnew.modelApi.notificaciones;

public class ConvocatoriaNtf {
    private String fechaInicio;
    private String titulo;
    private String descripcion;
    private String fechaCreacion;
    private String estado;
    private String instrucciones;
    private Plantilla template;

    // Default constructor
    public ConvocatoriaNtf() {
    }

    // Constructor with parameters
    public ConvocatoriaNtf(String fechaInicio, String titulo, String descripcion, String fechaCreacion, String estado, String instrucciones, Plantilla template) {
        this.fechaInicio = fechaInicio;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.instrucciones = instrucciones;
        this.template = template;
    }

    // Getters and Setters
    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }

    public Plantilla getTemplate() {
        return template;
    }

    public void setTemplate(Plantilla template) {
        this.template = template;
    }
}