package com.karmai.blog.consumer;

import com.karmai.blog.dto.EmailDTO;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author zhaokang03
 * @Date 2023/2/16 15:09
 */
@Component
public class MailConsumer {
    @Value("${spring.mail.username}")
    private String from;

    @Resource
    private JavaMailSender javaMailSender;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "mailQueue",durable = "true"),
                    exchange = @Exchange(value = "mainExchange",durable = "true",type = "topic"),
                    key = "mainRouterKey"
            )
    )
    @RabbitHandler
    public void send(@Payload EmailDTO emailDTO){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(emailDTO.getTo());
        message.setSubject(emailDTO.getSubject());
        message.setText(emailDTO.getText());
        javaMailSender.send(message);
    }
}
