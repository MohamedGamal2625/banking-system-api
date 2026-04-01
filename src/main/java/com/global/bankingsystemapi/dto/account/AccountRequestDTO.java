package com.global.bankingsystemapi.dto.account;

import com.global.bankingsystemapi.entity.enums.AccountStatus;
import com.global.bankingsystemapi.entity.enums.AccountType;
import lombok.*;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountRequestDTO {
    private Long CustomerId;
    private BigDecimal initialBalance;
    private AccountType accountType;
}
