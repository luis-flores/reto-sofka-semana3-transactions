package com.sofka.transactions.drivenAdapters.bus;

import com.google.gson.Gson;
import com.sofka.transactions.RabbitConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Sender;

@Component
public class RabbitMqPublisher {

    @Autowired
    private Sender sender;

    @Autowired
    private Gson gson;

    public void publishMessage(Object object){
        sender.send(Mono.just(new OutboundMessage(RabbitConfig.EXCHANGE_NAME,
            RabbitConfig.ROUTING_KEY_NAME, gson.toJson(object).getBytes()))).subscribe();
    }

    public void publishLog(String message) {
        publishLog(message, null);
    }

    public void publishLog(String message, Object object){
        String logMessage = "";

        if (!message.isEmpty())
            logMessage += message;
        if (object != null)
            logMessage += gson.toJson(object);

        sender.send(Mono.just(new OutboundMessage(RabbitConfig.EXCHANGE_NAME,
            RabbitConfig.ROUTING_KEY_LOG, logMessage.getBytes()))).subscribe();
    }

    public void publishError(Object object) {
        sender.send(Mono.just(new OutboundMessage(RabbitConfig.EXCHANGE_NAME,
            RabbitConfig.ROUTING_KEY_ERROR, gson.toJson(object).getBytes()))).subscribe();
    }
}
