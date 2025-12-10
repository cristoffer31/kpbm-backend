package com.kbcollection.resource;

import com.kbcollection.entity.Configuracion;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/configuracion")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConfiguracionResource {

    // Método auxiliar para obtener la única configuración existente
    private Configuracion obtenerConfiguracionUnica() {
        // Buscamos cualquier registro
        Configuracion config = Configuracion.findAll().firstResult();
        
        // Si no hay ninguno, creamos uno nuevo vacío
        if (config == null) {
            config = new Configuracion();
            config.nombreTienda = "KB Collection";
            config.telefonoVentas = "50300000000";
            config.telefonoContacto = "2200-0000";
            config.horarios = "Lunes a Viernes: 8am - 5pm";
            config.mapaUrl = "";
        }
        return config;
    }

    @GET
    @PermitAll
    public Response obtener() {
        return Response.ok(obtenerConfiguracionUnica()).build();
    }

    @PUT
    @Transactional
    @RolesAllowed("SUPER_ADMIN")
    public Response actualizar(Configuracion datos) {
        // 1. Buscamos la configuración existente (o null si no hay)
        Configuracion config = Configuracion.findAll().firstResult();

        // 2. Si no existe, creamos una instancia nueva y la guardamos
        if (config == null) {
            config = new Configuracion();
            config.persist(); // Guardamos para generar ID
        }

        // 3. Actualizamos los datos
        config.nombreTienda = datos.nombreTienda;
        config.telefonoVentas = datos.telefonoVentas;
        config.telefonoContacto = datos.telefonoContacto; // <--- NO OLVIDAR ESTE
        config.direccionTienda = datos.direccionTienda;
        config.emailContacto = datos.emailContacto;
        config.costoEnvioBase = datos.costoEnvioBase;
        config.horarios = datos.horarios;
        config.mapaUrl = datos.mapaUrl;
        config.costoEnvioBase = datos.costoEnvioBase;
        config.facebookUrl = datos.facebookUrl;
        config.instagramUrl = datos.instagramUrl;
        config.tiktokUrl = datos.tiktokUrl;
        
        // No es necesario llamar a persist() de nuevo si ya está gestionada (managed), 
        // pero por seguridad en Quarkus/Panache no daña.
        return Response.ok(config).build();
    }
}