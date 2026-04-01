package com.global.bankingsystemapi.service.impl;

import com.global.bankingsystemapi.dto.transaction.TransactionRequestDTO;
import com.global.bankingsystemapi.dto.transaction.TransactionResponseDTO;
import com.global.bankingsystemapi.entity.Account;
import com.global.bankingsystemapi.entity.Transaction;
import com.global.bankingsystemapi.entity.enums.TransactionType;
import com.global.bankingsystemapi.exception.ResourceNotFoundException;
import com.global.bankingsystemapi.repository.AccountRepo;
import com.global.bankingsystemapi.repository.TransactionRepo;
import com.global.bankingsystemapi.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {
    private final AccountRepo accountRepo;
    private final TransactionRepo transactionRepo;
    @Override
    public TransactionResponseDTO deposit(TransactionRequestDTO request) {
        Account account = getAccount(request.getTargetAccountId());
        validateAmount(request.getAmount());
        //deposit balance + amount
        account.setBalance(account.getBalance().add(request.getAmount()));
        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .transactionType(TransactionType.DEPOSIT)
                .timeStamp(LocalDateTime.now())
                .targetAccount(account)
                .build();
       Transaction saved = transactionRepo.save(transaction);
        return mapToResponse(saved);
    }

    @Override
    public TransactionResponseDTO withdraw(TransactionRequestDTO request) {
        Account account = getAccount(request.getSourceAccountId());

        if(account.getBalance().compareTo(request.getAmount()) < 0 )
            throw new ResourceNotFoundException("not enough balance");
        //withdraw
        account.setBalance(account.getBalance().subtract(request.getAmount()));

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .transactionType(TransactionType.WITHDRAW)
                .sourceAccount(account)
                .timeStamp(LocalDateTime.now())
                .build();
          Transaction saved = transactionRepo.save(transaction);
        return mapToResponse(saved);
    }

    @Override
    public TransactionResponseDTO transfer(TransactionRequestDTO request) {
        if(request.getSourceAccountId().equals(request.getTargetAccountId()))
            throw new IllegalArgumentException("cannot transfer to same account");

        Account source = getAccount(request.getSourceAccountId());
        Account target = getAccount(request.getTargetAccountId());
        validateAmount(request.getAmount());

        if (source.getBalance().compareTo(request.getAmount()) < 0)
            throw new IllegalArgumentException("Not enough balance");

        source.setBalance(source.getBalance().subtract(request.getAmount()));
        target.setBalance((target.getBalance().add(request.getAmount())));

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .transactionType(TransactionType.TRANSFER)
                .timeStamp(LocalDateTime.now())
                .sourceAccount(source)
                .targetAccount(target)
                .build();
        Transaction saved = transactionRepo.save(transaction);
        return mapToResponse(saved);
    }
    private Account getAccount(Long id) {
        return accountRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("Account not found with id :"+id));
    }
    private void validateAmount(BigDecimal amount){
        if (amount == null || amount.compareTo(amount.ZERO) <= 0 )
            throw new IllegalArgumentException("Amount must be greater than zero");
    }
    private TransactionResponseDTO  mapToResponse(Transaction transaction){
        return TransactionResponseDTO.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .type(transaction.getTransactionType())
                .sourceAccountId(transaction.getSourceAccount() != null? transaction.getSourceAccount().getId() : null)
                .targetAccountId(transaction.getTargetAccount() !=null? transaction.getTargetAccount().getId() : null)
                .createdAt(transaction.getTimeStamp())
                .build();
    }
}
