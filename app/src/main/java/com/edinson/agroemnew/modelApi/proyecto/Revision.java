package com.edinson.agroemnew.modelApi.proyecto;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Revision {

    @SerializedName("_id")
    private String id;

    @SerializedName("usuario")
    private String usuario;

    @SerializedName("seccion")
    private String seccion;

    @SerializedName("estado")
    private String estado;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("sugerencia")
    private String sugerencia;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("fechaRevision")
    private Date fechaRevision;

    @SerializedName("proyecto")
    private String proyecto;

    @SerializedName("__v")
    private int version;

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSugerencia() {
        return sugerencia;
    }

    public void setSugerencia(String sugerencia) {
        this.sugerencia = sugerencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaRevision() {
        return fechaRevision;
    }

    public void setFechaRevision(Date fechaRevision) {
        this.fechaRevision = fechaRevision;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
