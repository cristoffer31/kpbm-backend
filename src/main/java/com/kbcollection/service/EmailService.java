package com.kbcollection.service;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EmailService {

    @Inject
    Mailer mailer;

    // Método auxiliar para obtener la URL correcta (Nube o Local)
    private String getFrontendUrl() {
        String url = System.getenv("FRONTEND_URL");
        // Si no existe la variable (o estamos en local sin configurarla), 
        // usamos la de producción por seguridad para que los correos reales funcionen.
        if (url == null || url.trim().isEmpty()) {
            return "https://kpbm-frontend.onrender.com"; 
        }
        // Asegurar que no tenga barra al final para evitar dobles barras //
        if (url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        }
        return url;
    }

    public void enviarVerificacion(String email, String token) {
        String baseUrl = getFrontendUrl();
        String link = baseUrl + "/verificar?token=" + token;
        
        // Diseño KPBM Fucsia (#C2185B)
        String html = "<h1>Bienvenido a KPBM</h1>"
                + "<p>Gracias por registrarte. Para activar tu cuenta, haz clic en el siguiente enlace:</p>"
                + "<a href='" + link + "' style='background:#C2185B;color:white;padding:10px 20px;text-decoration:none;border-radius:5px;font-weight:bold;'>ACTIVAR CUENTA</a>";

        mailer.send(Mail.withHtml(email, "Activa tu cuenta - KPBM", html));
    }

    public void enviarRecuperacion(String email, String token) {
        String baseUrl = getFrontendUrl();
        String link = baseUrl + "/restablecer?token=" + token;
        
        // Diseño KPBM
        String html = "<h1>Recuperación de Contraseña</h1>"
                + "<p>Has solicitado cambiar tu clave en KPBM. Haz clic abajo para crear una nueva:</p>"
                + "<a href='" + link + "' style='background:#C2185B;color:white;padding:10px 20px;text-decoration:none;border-radius:5px;font-weight:bold;'>RESTABLECER CONTRASEÑA</a>"
                + "<p>Si no fuiste tú, ignora este mensaje.</p>";

        mailer.send(Mail.withHtml(email, "Recuperar Contraseña - KPBM", html));
    }

    public void enviarMensajeContacto(String nombre, String emailCliente, String asunto, String mensaje) {
        // Este es el correo del dueño
        String emailAdmin = "consultoriatecnologicaerazo@gmail.com"; 

        String html = "<h2>📩 Nuevo Mensaje de la Web KPBM</h2>"
                + "<p><strong>Cliente:</strong> " + nombre + "</p>"
                + "<p><strong>Correo del Cliente:</strong> " + emailCliente + "</p>"
                + "<p><strong>Asunto:</strong> " + asunto + "</p>"
                + "<hr/>"
                + "<h3>Mensaje:</h3>"
                + "<p>" + mensaje + "</p>";

        mailer.send(Mail.withHtml(emailAdmin, "Contacto Web KPBM: " + asunto, html));
    }
}