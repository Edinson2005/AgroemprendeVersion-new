package com.edinson.agroemnew.modelApi.notificaciones;

public class ProyectoNot {

    private String _id;
    private String title;
    private String body;
    private String url;
    private String estado;
    private Proyecto proyecto;  // Campo de tipo Proyecto

    // Getters y Setters

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
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

    public Comparable<Object> getFecha() {
        return null;
    }

    public static class Proyecto {
        private String _id;
        private String usuarioId;

        // Getters y Setters

        public String getId() {
            return _id;
        }

        public void setId(String _id) {
            this._id = _id;
        }

        public String getUsuarioId() {
            return usuarioId;
        }

        public void setUsuarioId(String usuarioId) {
            this.usuarioId = usuarioId;
        }
    }
}

