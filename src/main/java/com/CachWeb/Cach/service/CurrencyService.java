package com.CachWeb.Cach.service;

import com.CachWeb.Cach.entity.Currency;
import com.CachWeb.Cach.repository.CurrencyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyService {
    @Autowired
    private CurrencyRepository currencyRepository;

    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    @Transactional
    public void addCurrency(Currency currency) {
        // Additional validation or business logic if needed
        currencyRepository.save(currency);
    }

    @Transactional
    public void remove(Long id) {
        currencyRepository.deleteById(id);
    }
}
