package com.edinson.agroemnew.modelApi.notificaciones;

public class TokenRequest {
    private String token;
    private String title;
    private String body;
    private ConvocatoriaNtf convocatoria;  // Ensure this matches the type expected
    private String estado;

    // Updated constructor
    public TokenRequest(String token, String title, String body, ConvocatoriaNtf convocatoria, String estado) {
        this.token = token;
        this.title = title;
        this.body = body;
        this.convocatoria = convocatoria;  // Use the provided parameter
        this.estado = estado;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public ConvocatoriaNtf getConvocatoria() {
        return convocatoria;
    }

    public void setConvocatoria(ConvocatoriaNtf convocatoria) {
        this.convocatoria = convocatoria;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}