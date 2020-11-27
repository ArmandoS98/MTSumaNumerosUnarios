package com.aescttgt.mt;

public class Trancisiones {
    private String nombreEstado;
    private String lectura;
    private String cambios;
    private String estadoSiguiente;

    public Trancisiones() {
    }

    public Trancisiones(String nombreEstado, String lectura, String cambios, String estadoSiguiente) {
        this.nombreEstado = nombreEstado;
        this.lectura = lectura;
        this.cambios = cambios;
        this.estadoSiguiente = estadoSiguiente;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public String getLectura() {
        return lectura;
    }

    public void setLectura(String lectura) {
        this.lectura = lectura;
    }

    public String getCambios() {
        return cambios;
    }

    public void setCambios(String cambios) {
        this.cambios = cambios;
    }

    public String getEstadoSiguiente() {
        return estadoSiguiente;
    }

    public void setEstadoSiguiente(String estadoSiguiente) {
        this.estadoSiguiente = estadoSiguiente;
    }
}
