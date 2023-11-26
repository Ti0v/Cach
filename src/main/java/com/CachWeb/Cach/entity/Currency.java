package com.CachWeb.Cach.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;

    @OneToMany(mappedBy = "sourceCurrency", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ExchangeRate> sourceExchangeRates = new ArrayList<>();

    @OneToMany(mappedBy = "targetCurrency", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ExchangeRate> targetExchangeRates = new ArrayList<>();

    @OneToMany(mappedBy = "sourceCurrency", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ExchangeRequest> sourceExchangeRequests = new ArrayList<>();

    @OneToMany(mappedBy = "targetCurrency", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ExchangeRequest> targetExchangeRequests = new ArrayList<>();

    // Constructors, getters, and setters
}
