package com.global.bankingsystemapi.service.impl;

import com.global.bankingsystemapi.dto.account.AccountRequestDTO;
import com.global.bankingsystemapi.dto.account.AccountResponseDTO;
import com.global.bankingsystemapi.entity.Account;
import com.global.bankingsystemapi.entity.Customer;
import com.global.bankingsystemapi.entity.enums.AccountStatus;
import com.global.bankingsystemapi.exception.ResourceNotFoundException;
import com.global.bankingsystemapi.repository.AccountRepo;
import com.global.bankingsystemapi.repository.CustomerRepo;
import com.global.bankingsystemapi.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepo accountRepo;
    private final CustomerRepo customerRepo;

    @Override
    public AccountResponseDTO createAccount(AccountRequestDTO request) {
        Customer customer = customerRepo.findById(request.getCustomerId())
                .orElseThrow(()-> new ResourceNotFoundException("customer not found"));

        Account account = Account.builder()
                .balance(request.getInitialBalance())
                .accountType(request.getAccountType())
                .status(AccountStatus.ACTIVE)
                .accountNumber(generateAccountNumber())
                .customer(customer)
                .build();

        Account saved = accountRepo.save(account);
        return mapToResponse(saved);
    }

    @Override
    public AccountResponseDTO getAccountById(Long id) {
        return mapToResponse(getAccount(id));
    }

    @Override
    public List<AccountResponseDTO> getAllAccounts() {
        return accountRepo.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public AccountResponseDTO updateAccountStatus(Long id, AccountStatus status) {
        Account account = getAccount(id);
        account.setStatus(status);
        return  mapToResponse(account);
    }
    //helper methods
    private Account getAccount(Long id) {
        return accountRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("account not found"));
    }

    private AccountResponseDTO mapToResponse(Account account){
        return AccountResponseDTO.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .accountType(account.getAccountType())
                .status(account.getStatus())
                .customerId(account.getCustomer().getId())
                .build();
    }
    private String generateAccountNumber() {
        return "ACC-" + System.currentTimeMillis();
    }
}
