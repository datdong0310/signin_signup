package com.signin_signup.signin_signup_demo.Services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServices {
    @Autowired
    public JavaMailSender eMailSender;
    private static final Logger logger = LoggerFactory.getLogger(EmailServices.class);
    public void sendToken(String Token, String email){
        try{
              MimeMessage message = eMailSender.createMimeMessage();
              MimeMessageHelper helper = new MimeMessageHelper(message, true);  
              helper.setTo(email);
              helper.setSubject("Authentication Token");
              helper.setText(Token);

              eMailSender.send(message);
            } catch(MessagingException e){
                    logger.error("Fail to send email", e);
              };

    }
   
}
