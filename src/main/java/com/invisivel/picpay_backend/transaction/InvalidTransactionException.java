package com.invisivel.picpay_backend.transaction;

public class InvalidTransactionException extends RuntimeException {
    public InvalidTransactionException(String message){
        super(message);
    }
}
