package com.CachWeb.Cach.service;

import com.CachWeb.Cach.entity.Currency;
import com.CachWeb.Cach.entity.ExchangeRate;
import com.CachWeb.Cach.repository.ExchangeRateRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ExchangeRateService {
    @Autowired
    private ExchangeRateRepository exchangeRateRepository;

    @Transactional
    public void save(ExchangeRate exchangeRate) {
        exchangeRateRepository.save(exchangeRate);
    }

    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateRepository.findAll();
    }



    @Transactional
    public List<Currency> getTargetCurrencies(Currency sourceCurrency) {
        // Retrieve target currencies based on the selected source currency
        return exchangeRateRepository.findDistinctTargetCurrenciesBySourceCurrency(sourceCurrency);
    }
    @Transactional
    public BigDecimal getExchangeRate(Currency sourceCurrency, Currency targetCurrency) {
        ExchangeRate exchangeRate = exchangeRateRepository.findBySourceCurrencyAndTargetCurrency(sourceCurrency, targetCurrency);

        if (exchangeRate != null && exchangeRate.getRate() != null) {
            return exchangeRate.getRate();
        } else {


            throw new RuntimeException("Exchange rate not available for the specified currencies");
        }

    }
         public void remove(Long id ){
        exchangeRateRepository.deleteById(id);
    }
}
