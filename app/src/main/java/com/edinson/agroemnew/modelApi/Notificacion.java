package com.edinson.agroemnew.modelApi;

public class Notificacion {
    private String id;
    private String title;
    private String body;
    private String url;
    private String estado;
    private boolean vista;


    public Notificacion() {}


    public Notificacion(String id, String title, String body, String url, String estado, String fechaCierre) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.url = url;
        this.estado = estado;
        this.vista = false;


    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }



    public boolean isVista() {
        return vista;
    }

    public void setVista(boolean vista) {
        this.vista = vista;
    }
}