package com.invisivel.picpay_backend.notification;

import org.springframework.stereotype.Service;

import com.invisivel.picpay_backend.transaction.Transaction;

@Service
public class NotificationService {
    //Utilizar o kafkatemplate para fazer o envio das sms e para isso criamos duas classes (producer e consumer)
    private final NotificationProducer notificationProducer;

    public NotificationService(NotificationProducer notificationProducer){
        this.notificationProducer = notificationProducer;
    }

    public void notify(Transaction transaction){
        //Operação assyncrona porque o serviço é intermitente
        notificationProducer.sendNotification(transaction);

    }
}
