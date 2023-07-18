package com.example.emailservice;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Service
public class RabbitListener implements MessageListener {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onMessage(Message message) {

        System.out.println("New message received from "
                + message.getMessageProperties().getConsumerQueue()
                + ": "
                + new String(message.getBody()));

        String email = "yihey@andrew.cmu.edu";
        String code = "123456";

        try {
            String body = new String(message.getBody(), StandardCharsets.UTF_8);
            JsonNode jsonNode = objectMapper.readTree(body);

            email = jsonNode.get("email").asText();
            code = jsonNode.get("code").asText();

        } catch (IOException e) {
            // Handle JSON parsing error
            System.out.println("There's some parse error");
        }

        // sending email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("noreply@baeldung.com");
        mailMessage.setTo(email);
        mailMessage.setSubject("Verification Code");
        mailMessage.setText("Your verification code is: " + code);

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
