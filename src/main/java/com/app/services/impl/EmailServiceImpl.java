package com.app.services.impl;

import com.app.persistence.entities.students.StudentEntity;
import com.app.persistence.repositories.StudentRepository;
import com.app.services.IEmailService;
import com.app.util.JwtUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@PropertySource("classpath:email.properties")
public class EmailServiceImpl implements IEmailService {

    @Value("${spring.mail.username}")
    private String email;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private StudentRepository studentRepository;
    @Qualifier("templateEngine")
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public void sendEmail(String[] toUser, String subject, String message) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(email);
        mailMessage.setTo(toUser);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailSender.send(mailMessage);

    }

    @Override
    public void sendEmailWithFile(String[] toUser, String subject, String message, File file) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

        mimeMessageHelper.setFrom(email);
        mimeMessageHelper.setTo(toUser);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(message);
        mimeMessageHelper.addAttachment(file.getName(), file);
        mailSender.send(mimeMessage);
    }

    @Override
    public void sendInvitations(String[] emails, Long groupId) throws MessagingException {
        for (String email : emails) {
            Optional<StudentEntity> studentOptional = studentRepository.findStudentByEmail(email);

            if (studentOptional.isPresent()) {
                StudentEntity studentEntity = studentOptional.get();
                String nombreEstudiante = studentEntity.getName();

                String token = jwtUtils.generateInvitationToken(email, groupId);
                String link = "http://localhost:9090/auth/register/student?token=" + token;

                Map<String, Object> templateVariables = new HashMap<>();
                templateVariables.put("nombreEstudiante", nombreEstudiante);
                templateVariables.put("link", link);
                String messageContent = buildInvitationMessage(templateVariables);

                sendHtmlEmail(new String[]{email}, "Notificación de inscripción al curso", messageContent);
            } else {
                throw new RuntimeException("Student with email " + email + " not exists");
            }
        }
    }

    private String buildInvitationMessage(Map<String, Object> templateVariables) {
        Context context = new Context();
        context.setVariables(templateVariables);
        return templateEngine.process("email", context);
    }

    private void sendHtmlEmail(String[] toUser, String subject, String message) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        try {
            helper.setTo(toUser);
            helper.setSubject(subject);
            helper.setText(message, true); // true indica que el mensaje es HTML
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
