package com.invisivel.picpay_backend.transaction;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.invisivel.picpay_backend.authorization.AuthorizerService;
import com.invisivel.picpay_backend.notification.NotificationService;
import com.invisivel.picpay_backend.wallet.Wallet;
import com.invisivel.picpay_backend.wallet.WalletRepository;
import com.invisivel.picpay_backend.wallet.WalletType;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final AuthorizerService authorizerService;
    private final NotificationService notificationService;

    public TransactionService(TransactionRepository transactionRepository, WalletRepository walletRepository, 
                                AuthorizerService authorizerService, NotificationService notificationService){
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.authorizerService = authorizerService;
        this.notificationService = notificationService;
    }

    @Transactional
    public Transaction create(Transaction transaction){
        // 1 - Validar segundo as regras de negócio
        validate(transaction);

        // 2 - criar a transação
        var newTransaction = transactionRepository.save(transaction);

        // 3 - debitar da carteira
        var walletPayer = walletRepository.findById(transaction.payer()).get();
            walletRepository.save(walletPayer.debit(transaction.value()));

        // 3.1 - creditar na carteira
        var walletPayee = walletRepository.findById(transaction.payee()).get();
            walletRepository.save(walletPayee.credit(transaction.value()));

        // 4 - chamar serviços externos
        // 4.1 - Integração externa ao serviço de autorização (authorize transaction)
        authorizerService.authorize(transaction);

        // 4.2 notificação
        // Send notification
        //notificationService.notify(transaction);

        return newTransaction;
    }

    /* 
     * - if the payer has a commom wallet (pagador)
     * - if the payer has enough balance (saldo duficiente)
     * - if the payer is not the payee
     * - if the payee has a store wallet (receptor ou lojista)
    */
    private void validate(Transaction transaction){
        walletRepository.findById(transaction.payee())
            .map(payee -> walletRepository.findById(transaction.payer())
                .map(payer -> isTransactionValid(transaction, payer) ? transaction : null)
                .orElseThrow(() -> new InvalidTransactionException("Invalid transation - " + transaction)))
            .orElseThrow(() -> new InvalidTransactionException("Invalid transation - " + transaction));

    }

    private boolean isTransactionValid(Transaction transaction, Wallet payer) {
        return payer.type() == WalletType.COMUM.getValue() && 
            payer.balance().compareTo(transaction.value()) >= 0 && 
             !payer.id().equals(transaction.payee());
    }

    public List<Transaction> list(){
        return transactionRepository.findAll();
    }
    
}
