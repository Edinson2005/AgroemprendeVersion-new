package com.edinson.agroemnew.modelApi.notificaciones;

public class NotiConvocatorias {
    private String _id; // Cambiado a _id para que coincida con el JSON
    private String title;
    private String body;
    private String estado;
    private Convocatoria convocatoria;

    public NotiConvocatorias() {}

    public NotiConvocatorias(String _id, String title, String body, String estado, Convocatoria convocatoria) {
        this._id = _id;
        this.title = title;
        this.body = body;
        this.estado = estado;
        this.convocatoria = convocatoria;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Convocatoria getConvocatoria() {
        return convocatoria;
    }

    public void setConvocatoria(Convocatoria convocatoria) {
        this.convocatoria = convocatoria;
    }
}