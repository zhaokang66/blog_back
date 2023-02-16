package com.karmai.blog.consumer;

import com.karmai.blog.entity.SysMenu;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author zhaokang03
 * @Date 2023/2/15 17:03
 */
@Component
public class TestConsumer {

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "testQueue",durable = "true"),
                    exchange = @Exchange(value = "testExchange",durable = "true",type = "topic"),
                    key = "test.#"
            )
    )
    @RabbitHandler
    public void onListen(@Payload List<SysMenu> data, @Headers Map<String ,Object> headers, Channel channel) throws Exception{
        System.out.println(data);
        // 如果消息的签收模式是手动
        Long deliverTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(deliverTag,false);// 第二个参数代表是否支持批量签收
    }
}
