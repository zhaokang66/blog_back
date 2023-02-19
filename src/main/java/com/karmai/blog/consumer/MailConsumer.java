package com.karmai.blog.consumer;

import com.karmai.blog.dto.EmailDTO;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

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

    /**
     * 消费者，发送普通邮件
     * @param emailDTO
     * @param headers
     * @param channel
     * @throws Exception
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "mailQueue",durable = "true"),
                    exchange = @Exchange(value = "mainExchange",durable = "true",type = "topic"),
                    key = "mainRouterKey"
            )
    )
    @RabbitHandler
    public void send(@Payload EmailDTO emailDTO, @Headers Map<String ,Object> headers, Channel channel) throws Exception{
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(emailDTO.getTo());
        message.setSubject(emailDTO.getSubject());
        message.setText(emailDTO.getText());
        javaMailSender.send(message);
        Long deliverTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliverTag,false);// 第二个参数代表是否支持批量签收
    }
}
