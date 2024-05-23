package com.app.controllers;

import com.app.controllers.dto.MailDTO;
import com.app.controllers.dto.MailFileDTO;
import com.app.controllers.dto.request.MailInvitationRequest;
import com.app.services.IEmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    @Autowired
    private IEmailService iEmailService;

    public ResponseEntity<?> sendMail(String to, String subject, String body) {
        return null;
    }

    @PostMapping("/send-message")
    public ResponseEntity<?> reciveRequestEmail(@RequestBody MailDTO mailDTO) {

        System.out.println("Mensaje Recibido" + mailDTO.toString());

        iEmailService.sendEmail(mailDTO.toUser(), mailDTO.subject(), mailDTO.message());

        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-message-file")
    public ResponseEntity<?> reciveRequestEmailWithFile(@ModelAttribute MailFileDTO mailFileDTO) {

        System.out.println("Mensaje Recibido" + mailFileDTO.toString());

        try {
            String fileName = mailFileDTO.file().getOriginalFilename();

            Path path = Paths.get("src/main/resources/files/img/" + fileName);

            Files.createDirectories(path.getParent());
            Files.copy(mailFileDTO.file().getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            File file = path.toFile();

            iEmailService.sendEmailWithFile(mailFileDTO.toUser(), mailFileDTO.subject(), mailFileDTO.message(), file);

            Map<String, String> response = new HashMap<>();
            response.put("status", "ok");
            response.put("file", fileName);

            return ResponseEntity.ok(response);

        }catch (Exception e){
            throw new RuntimeException("Error al enviar el email" + e.getMessage());
        }
    }

    @PostMapping("/send-invitations")
    public ResponseEntity<?> sendInvitations(@RequestBody MailInvitationRequest invitationRequest) throws MessagingException {
        String[] emails = invitationRequest.emails();
        try {
            iEmailService.sendInvitations(emails);
            Map<String, String> response = new HashMap<>();
            response.put("status", "invitations sent");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            // Devuelve una respuesta con el mensaje de error
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
