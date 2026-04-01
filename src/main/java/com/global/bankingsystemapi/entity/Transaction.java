package com.global.bankingsystemapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.global.bankingsystemapi.entity.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "source_account_id")
    @JsonIgnore
    private Account sourceAccount;  // For withdraw or transfer (money leaving)

    @ManyToOne
    @JoinColumn(name = "target_account_id")
    @JsonIgnore
    private Account targetAccount;  // For deposit or transfer (money received)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private LocalDateTime timeStamp;

}
