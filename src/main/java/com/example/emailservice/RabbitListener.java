package com.example.emailservice;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class RabbitListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        System.out.println("New message received from "
                + message.getMessageProperties().getConsumerQueue()
                + ": "
                + new String(message.getBody()));

        // sending email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("noreply@baeldung.com");
        mailMessage.setTo("yihey@andrew.cmu.edu");
        mailMessage.setSubject("Verification Code");
        mailMessage.setText("Your verification code is: 1234");

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("yiheyoung@gmail.com");
        mailSender.setPassword("ititsgaoxmbphrhl");
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        mailSender.send(mailMessage);
    }
}
