package com.karmai.blog.consumer;

import com.karmai.blog.entity.SysMenu;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

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
    public void onListen(@Payload List<SysMenu> data) {
        System.out.println(data);
    }
}
