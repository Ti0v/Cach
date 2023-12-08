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
public class ExchangeRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "source_currency_id", nullable = false)
    private Currency sourceCurrency;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "target_currency_id", nullable = false)
    private Currency targetCurrency;

    private BigDecimal sendingAmount;
    private BigDecimal receivingAmount;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "image_id")
    private Image image;



    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean archived;


}
