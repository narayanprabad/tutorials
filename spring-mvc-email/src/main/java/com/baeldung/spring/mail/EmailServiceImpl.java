package com.baeldung.spring.mail;

import com.baeldung.spring.web.dto.MailObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

/**
 * Created by Olga on 7/15/2016.
 */
@Component
public class EmailServiceImpl implements EmailService {

    @Autowired
    public JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            emailSender.send(message);
        } catch (MailException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void sendSimpleMessageUsingTemplate(String to,
                                               String subject,
                                               SimpleMailMessage template,
                                               String ...templateArgs) {
        String text = String.format(template.getText(), templateArgs);
        sendSimpleMessage(to, subject, text);
    }

    @Override
    public void sendMessageWithAttachment(String to,
                                          String subject,
                                          String text,
                                          String pathToAttachment) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            // pass 'true' to the constructor to create a multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
            helper.addAttachment("Invoice", file);

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /*public void sendMail(MimeMessage message) {
        try {
            emailSender.send(message);
        } catch (MailException exception) {
            exception.printStackTrace();
        }
    }

    public MimeMessage createMessageWithAttachment(MailObject mailObject) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            // pass 'true' to the constructor to create a multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(mailObject.getFrom());
            helper.setTo(mailObject.getTo());
            helper.setSubject(mailObject.getSubject());
            helper.setText(mailObject.getText());

            // attach a sample image attachment
            FileSystemResource file = new FileSystemResource(new File("c:/attachment.jpg"));
            helper.addAttachment("Attachment.jpg", file);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }*/

    /*@Autowired
    public SimpleMailMessage template;

    public SimpleMailMessage createSimpleMailMessage(MailObject mailObject) {
        SimpleMailMessage mailMessage = new SimpleMailMessage(template);

        mailMessage.setFrom(mailObject.getFrom());
        mailMessage.setTo(mailObject.getTo());
        mailMessage.setSubject(mailObject.getSubject());
        mailMessage.setText(String.format(template.getText(), mailObject.getText()));

        return mailMessage;
    }*/
}
