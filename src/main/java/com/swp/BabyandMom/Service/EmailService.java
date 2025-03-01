package com.swp.BabyandMom.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String userEmail, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("huydk123pro@gmail.com");
        message.setTo(userEmail);
        message.setSubject(subject);
        message.setText(text);


        mailSender.send(message);
    }
}
