package com.kbcollection.dto;

public class ReporteDTO {
    public String etiqueta;
    public Double valor;
    public Long cantidad;

    // Constructor flexible para evitar errores de cast
    public ReporteDTO(String etiqueta, Double valor, Long cantidad) {
        this.etiqueta = etiqueta;
        this.valor = valor != null ? valor : 0.0;
        this.cantidad = cantidad != null ? cantidad : 0L;
    }
    
    // Constructor alternativo por si Hibernate devuelve Number
    public ReporteDTO(String etiqueta, Number valor, Number cantidad) {
        this.etiqueta = etiqueta;
        this.valor = valor != null ? valor.doubleValue() : 0.0;
        this.cantidad = cantidad != null ? cantidad.longValue() : 0L;
    }
}