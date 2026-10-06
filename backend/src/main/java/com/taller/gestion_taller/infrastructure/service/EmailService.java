package com.taller.gestion_taller.infrastructure.service;

import com.taller.gestion_taller.domain.exception.BusinessErrors;
import com.taller.gestion_taller.domain.exception.BusinessRunTimeException;
import com.taller.gestion_taller.domain.service.NotificadorCliente;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

@Slf4j
@Service
public class EmailService implements NotificadorCliente {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void notificarPorEmail(String email, String nombreCliente, String patenteVehiculo) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Es momento de hacer el service - " + patenteVehiculo);
            helper.setText(construirCuerpoHtml(nombreCliente, patenteVehiculo), true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("No se pudo enviar el correo de alerta de service a {}", email, e);
            throw new BusinessRunTimeException(BusinessErrors.errorEnvioEmail());
        }
    }

    private String construirCuerpoHtml(String nombreCliente, String patenteVehiculo) {
        String nombreSeguro = HtmlUtils.htmlEscape(nombreCliente);
        String patenteSegura = HtmlUtils.htmlEscape(patenteVehiculo);

        return """
            <!DOCTYPE html>
            <html lang="es">
            <head>
              <meta charset="UTF-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0">
              <title>Es momento de hacer el service</title>
            </head>
            <body style="margin:0; padding:0; background-color:#f4f4f5; font-family:Arial, Helvetica, sans-serif;">
              <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f4f4f5; padding:24px 0;">
                <tr>
                  <td align="center">
                    <table role="presentation" width="480" cellpadding="0" cellspacing="0" style="background-color:#ffffff; border-radius:8px; overflow:hidden; box-shadow:0 1px 3px rgba(0,0,0,0.08);">
                      <tr>
                        <td style="background-color:#1e3a8a; padding:20px 32px;">
                          <span style="color:#ffffff; font-size:20px; font-weight:bold; letter-spacing:0.5px;">G.M.A</span>
                        </td>
                      </tr>
                      <tr>
                        <td style="padding:32px;">
                          <p style="margin:0 0 4px; font-size:13px; font-weight:bold; letter-spacing:0.5px; text-transform:uppercase; color:#1e3a8a;">
                            Recordatorio de service
                          </p>
                          <p style="margin:0 0 16px; font-size:16px; color:#111827;">Hola %s,</p>
                          <p style="margin:0 0 16px; font-size:15px; color:#374151; line-height:1.5;">
                            Te recordamos que el service de tu vehículo
                            <strong style="color:#111827;">%s</strong>
                            ya está en fecha o kilometraje de service.
                          </p>
                          <p style="margin:0 0 24px; font-size:15px; color:#374151; line-height:1.5;">
                            Contactanos para coordinar un turno y mantener tu vehículo al día.
                          </p>
                          <hr style="border:none; border-top:1px solid #e5e7eb; margin:0 0 20px;">
                          <p style="margin:0; font-size:13px; color:#9ca3af;">
                            Este es un correo automático, no es necesario que lo respondas.
                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td style="background-color:#f9fafb; padding:16px 32px; border-top:1px solid #e5e7eb;">
                          <span style="font-size:12px; color:#9ca3af;">G.M.A. Gestión y Mantenimiento Automotriz</span>
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </body>
            </html>
            """.formatted(nombreSeguro, patenteSegura);
    }
}
