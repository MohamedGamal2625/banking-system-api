package com.global.bankingsystemapi.dto.transaction;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDTO {
    private Long sourceAccountId;   // used for withdraw & transfer
    private Long targetAccountId;   // used for deposit & transfer
    private BigDecimal amount;
}
