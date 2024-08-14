package com.edinson.agroemnew.modelApi;

public class ProyectoNot {

    private String titulo;
    private String fecha;
    private String descripcion;
    private String estado;

    public ProyectoNot() {}


    public ProyectoNot(String titulo, String fecha, String descripcion, String estado) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.estado = estado;
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}