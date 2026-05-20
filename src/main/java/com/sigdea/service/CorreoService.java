package com.sigdea.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class CorreoService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreo(String destino,
                             String asunto,
                             String mensaje) {

        SimpleMailMessage correo =
                new SimpleMailMessage();

        correo.setTo(destino);
        correo.setSubject(asunto);
        correo.setText(mensaje);

        correo.setFrom("anamariamoca@ufps.edu.co");

        mailSender.send(correo);
    }
}