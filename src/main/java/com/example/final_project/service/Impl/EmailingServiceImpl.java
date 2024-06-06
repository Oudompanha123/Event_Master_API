package com.example.final_project.service.Impl;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailingServiceImpl {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Async
    public void sendMail(String email, String otp) throws MessagingException {
        Context context = new Context();
        context.setVariable("content", otp);
        String processedString = templateEngine.process("template", context);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setSubject("Registered please verify with OTP.");
        mimeMessageHelper.setText(processedString, true);
        ClassPathResource image = new ClassPathResource("image/logo.png");
        mimeMessageHelper.addInline("logo", image);
        mimeMessageHelper.setFrom(fromMail);
        mimeMessageHelper.setTo(email);
        mailSender.send(mimeMessage);
    }

    @Async
    public void approveMail(String email) throws MessagingException {
        Context context = new Context();
        String processedString = templateEngine.process("approve", context);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setSubject("Approve From Admin.");
        mimeMessageHelper.setText(processedString, true);
        ClassPathResource image = new ClassPathResource("image/logo.png");
        mimeMessageHelper.addInline("logo", image);
        mimeMessageHelper.setFrom(fromMail);
        mimeMessageHelper.setTo(email);
        mailSender.send(mimeMessage);
    }
}
