package com.invisivel.picpay_backend.authorization;

public class UnauthorizedTransactionException extends RuntimeException {

    public UnauthorizedTransactionException(String message){
        super(message);
    }
    
}
