package com.CachWeb.Cach.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "source_currency_id", nullable = false)
    private Currency sourceCurrency;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "target_currency_id", nullable = false )
    private Currency targetCurrency;

    private BigDecimal rate;

}

