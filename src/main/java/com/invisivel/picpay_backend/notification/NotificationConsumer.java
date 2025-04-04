package com.invisivel.picpay_backend.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.invisivel.picpay_backend.transaction.Transaction;

@Service
public class NotificationConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Notification.class);

    private RestClient restClient;

    public NotificationConsumer(RestClient.Builder builder){
        this.restClient = builder
            .baseUrl("https://util.devi.tools/api/v1/notify")
            .build();
    }

    @KafkaListener(topics = "transaction-notification", groupId = "picpay-backend")
    public void receiveNotification(Transaction transaction){
        LOGGER.info("Notifying transaction: {}", transaction);

        var response = restClient.get()
            .retrieve()
            .toEntity(Notification.class);

        if(response.getStatusCode().isError() || !response.getBody().message())
            throw new NotificationException("Error send notification!");
        
        LOGGER.info("transaction notifyed: {}", transaction);
    }
}
