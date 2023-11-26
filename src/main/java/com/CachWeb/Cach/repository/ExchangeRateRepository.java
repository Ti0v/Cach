package com.CachWeb.Cach.repository;

import com.CachWeb.Cach.entity.Currency;
import com.CachWeb.Cach.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
     @Query("SELECT DISTINCT er.targetCurrency FROM ExchangeRate er WHERE er.sourceCurrency = :sourceCurrency")
     List<Currency> findDistinctTargetCurrenciesBySourceCurrency(@Param("sourceCurrency") Currency sourceCurrency);
     ExchangeRate findBySourceCurrencyAndTargetCurrency(Currency sourceCurrency, Currency targetCurrency);
}

