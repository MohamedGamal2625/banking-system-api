package com.global.bankingsystemapi.dto.account;

import com.global.bankingsystemapi.entity.enums.AccountStatus;
import com.global.bankingsystemapi.entity.enums.AccountType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponseDTO {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private AccountType accountType;
    private AccountStatus status;
    private Long customerId;
}
