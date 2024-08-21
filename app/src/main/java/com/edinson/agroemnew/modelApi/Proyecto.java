package com.edinson.agroemnew.modelApi;

import java.util.List;

public class Proyecto {
    private String _id;
    private String titulo;
    private String fecha;
    private String estado;
    private String descripcion;
    private Usuario usuarioId;
    private List<Seccion> secciones;  // Cambiado a List<Seccion>
    private List<Revision> revisiones; // Cambiado a List<Revision>
    private int __v;
    private String image;
    private List<String> files;

    // Getters y setters
    public String get_id() { return _id; }
    public void set_id(String _id) { this._id = _id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Usuario getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Usuario usuarioId) { this.usuarioId = usuarioId; }
    public List<Seccion> getSecciones() { return secciones; }
    public void setSecciones(List<Seccion> secciones) { this.secciones = secciones; }
    public List<Revision> getRevisiones() { return revisiones; }
    public void setRevisiones(List<Revision> revisiones) { this.revisiones = revisiones; }
    public int get__v() { return __v; }
    public void set__v(int __v) { this.__v = __v; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public List<String> getFiles() { return files; }
    public void setFiles(List<String> files) { this.files = files; }
}