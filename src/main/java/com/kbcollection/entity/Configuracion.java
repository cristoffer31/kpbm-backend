package com.kbcollection.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Configuracion extends PanacheEntity {
    public String nombreTienda;
    public String telefonoVentas;
    public String telefonoContacto;
    public String direccionTienda;
    public String emailContacto;
    public double costoEnvioBase;
    
    // --- NUEVOS CAMPOS ---
    public String horarios;
    
    @Column(columnDefinition = "TEXT") // Permite textos largos para el iframe
    public String mapaUrl;
    // ---------------------

    public String facebookUrl;
    public String instagramUrl;
    public String tiktokUrl;
}