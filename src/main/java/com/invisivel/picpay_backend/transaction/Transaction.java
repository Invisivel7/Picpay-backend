package com.invisivel.picpay_backend.transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("TRANSACTIONS")
public record Transaction(
    @Id Long id,
    Long payer, //Id do pagador
    Long payee, //Id de quem está recebendo o pagamento
    BigDecimal value,
    @CreatedDate LocalDateTime createdAt) {

    public Transaction {
        value = value.setScale(2);
    }
    
}
