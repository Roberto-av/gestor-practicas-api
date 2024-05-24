package com.app.services;

import jakarta.mail.MessagingException;

import java.io.File;

public interface IEmailService {
    void sendEmail(String[] toUser, String subject, String message);
    void sendEmailWithFile(String[] toUser, String subject, String message, File file) throws MessagingException;
    void sendInvitations(String[] emails, Long groupId) throws MessagingException;
}
