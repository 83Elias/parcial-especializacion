package com.dh.movie.publisher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMQProducer {

    @Value("${spring.mq.exchange}")
    private String exchange;

    @Value("${spring.mq.key-routing}")
    private String rountinKey;

    private RabbitTemplate rabbitTemplate;


    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message){
         log.info("Message sent to MQ queue with content : [{}]",message);
         rabbitTemplate.convertAndSend(exchange,rountinKey,message);
    }


}
