package com.edinson.agroemnew.modelApi;

public class UserUpdate {
    private String nombre;
    private String apellido;
    private String email;
    private String numIdentificacion;
    private String telefono;
    private String fechaNacimiento;


    public UserUpdate() {
    }

    public UserUpdate(String nombre, String apellido, String email, String numIdentificacion, String telefono, String fechaNacimiento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.numIdentificacion = numIdentificacion;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;


    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumIdentificacion() {
        return numIdentificacion;
    }

    public void setNumIdentificacion(String numIdentificacion) {
        this.numIdentificacion = numIdentificacion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }



}
