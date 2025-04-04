package com.invisivel.picpay_backend.transaction;

import org.springframework.data.repository.ListCrudRepository;

public interface TransactionRepository extends ListCrudRepository<Transaction, Long> {
    //ListCrudRepository trabalha com lista e não com iterable (pra não ter de fazer a conversão)
}
