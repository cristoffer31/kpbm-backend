package com.kbcollection.resource;

import com.kbcollection.dto.ReporteDTO;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.time.LocalDate; // <--- Importante
import java.util.List;

@Path("/api/reportes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"ADMIN", "SUPER_ADMIN"})
public class ReporteResource {

    @Inject
    EntityManager em;

    // 1. VENTAS POR CATEGORÍA
    @GET
    @Path("/categorias")
    public List<ReporteDTO> ventasPorCategoria(
            @QueryParam("fechaInicio") String inicio, 
            @QueryParam("fechaFin") String fin) {
        
        // Convertimos Strings a LocalDate para evitar el error de tipos
        LocalDate dateInicio = (inicio != null && !inicio.isBlank()) ? LocalDate.parse(inicio) : null;
        LocalDate dateFin = (fin != null && !fin.isBlank()) ? LocalDate.parse(fin) : null;

        String sql = "SELECT new com.kbcollection.dto.ReporteDTO(c.nombre, SUM(pi.precioUnitario * pi.cantidad), SUM(CAST(pi.cantidad AS long))) " +
                     "FROM PedidoItem pi " +
                     "JOIN pi.producto p " +
                     "JOIN p.category c " +
                     "JOIN pi.pedido ped " +
                     "WHERE ped.status <> 'CANCELADO' ";
        
        // Usamos parámetros posicionales o nombrados para mayor seguridad
        if (dateInicio != null) sql += "AND date(ped.fecha) >= :inicio ";
        if (dateFin != null)    sql += "AND date(ped.fecha) <= :fin ";
        
        sql += "GROUP BY c.nombre ORDER BY SUM(pi.precioUnitario * pi.cantidad) DESC";

        var query = em.createQuery(sql, ReporteDTO.class);
        
        if (dateInicio != null) query.setParameter("inicio", java.sql.Date.valueOf(dateInicio));
        if (dateFin != null)    query.setParameter("fin", java.sql.Date.valueOf(dateFin));

        return query.getResultList();
    }

    // 2. TOP CLIENTES
    @GET
    @Path("/clientes")
    public List<ReporteDTO> topClientes(
            @QueryParam("fechaInicio") String inicio, 
            @QueryParam("fechaFin") String fin) {

        LocalDate dateInicio = (inicio != null && !inicio.isBlank()) ? LocalDate.parse(inicio) : null;
        LocalDate dateFin = (fin != null && !fin.isBlank()) ? LocalDate.parse(fin) : null;

        String sql = "SELECT new com.kbcollection.dto.ReporteDTO(u.nombre, SUM(ped.total), COUNT(ped.id)) " +
                     "FROM Pedido ped " +
                     "JOIN ped.usuario u " +
                     "WHERE ped.status <> 'CANCELADO' ";

        if (dateInicio != null) sql += "AND date(ped.fecha) >= :inicio ";
        if (dateFin != null)    sql += "AND date(ped.fecha) <= :fin ";

        sql += "GROUP BY u.nombre ORDER BY SUM(ped.total) DESC";

        var query = em.createQuery(sql, ReporteDTO.class);

        if (dateInicio != null) query.setParameter("inicio", java.sql.Date.valueOf(dateInicio));
        if (dateFin != null)    query.setParameter("fin", java.sql.Date.valueOf(dateFin));

        return query.setMaxResults(10).getResultList();
    }
}