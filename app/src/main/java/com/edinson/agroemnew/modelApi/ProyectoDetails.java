package com.edinson.agroemnew.modelApi;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProyectoDetails {
    @SerializedName("_id")
    private String id;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("fecha")
    private String fecha;

    @SerializedName("estado")
    private String estado;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("usuarioId")
    private Usuario usuarioId;

    @SerializedName("files")
    private List<String> files;

    @SerializedName("secciones")
    private List<Seccion> secciones;

    @SerializedName("revisiones")
    private List<String> revisiones;

    @SerializedName("__v")
    private int version;

    // Getters y setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Usuario getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Usuario usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public List<Seccion> getSecciones() {
        return secciones;
    }

    public void setSecciones(List<Seccion> secciones) {
        this.secciones = secciones;
    }

    public List<String> getRevisiones() {
        return revisiones;
    }

    public void setRevisiones(List<String> revisiones) {
        this.revisiones = revisiones;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}