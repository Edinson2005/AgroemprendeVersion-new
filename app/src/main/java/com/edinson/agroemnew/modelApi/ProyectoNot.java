package com.edinson.agroemnew.modelApi;

import java.util.List;

public class ProyectoNot {
    private String title; // Correspondiente a "title" en el JSON
    private String body; // Correspondiente a "body" en el JSON
    private String url; // Correspondiente a "url" en el JSON
    private String estado; // Correspondiente a "estado" en el JSON
    private Proyecto proyecto; // Correspondiente a "proyecto" en el JSON

    // Getters y Setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }
}