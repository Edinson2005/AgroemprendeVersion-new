package com.edinson.agroemnew.modelApi.proyecto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Seccion {

    @SerializedName("_id")
    private String id;

    @SerializedName("proyecto")
    private String proyecto;

    @SerializedName("tipoSeccion")
    private List<TipoSeccion> tipoSeccion;

    @SerializedName("fechaCreacion")
    private String fechaCreacion;

    @SerializedName("__v")
    private int version;

    // Getters y setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProyecto() {
        return proyecto;
    }

    public void setProyecto(String proyecto) {
        this.proyecto = proyecto;
    }

    public List<TipoSeccion> getTipoSeccion() {
        return tipoSeccion;
    }

    public void setTipoSeccion(List<TipoSeccion> tipoSeccion) {
        this.tipoSeccion = tipoSeccion;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    // Clase interna para TipoSección
    public static class TipoSeccion {
        @SerializedName("nombre")
        private String nombre;

        @SerializedName("contenido")
        private String contenido;

        // Getters y setters

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getContenido() {
            return contenido;
        }

        public void setContenido(String contenido) {
            this.contenido = contenido;
        }
    }
}

