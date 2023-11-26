package com.CachWeb.Cach.controller;

import com.CachWeb.Cach.entity.Currency;
import com.CachWeb.Cach.service.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class ExchangeRateController {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @GetMapping("/getExchangeRate")
    @ResponseBody
    public BigDecimal getExchangeRate(@RequestParam Long sourceCurrencyId, @RequestParam Long targetCurrencyId) {
        Currency sourceCurrency = new Currency();
        sourceCurrency.setId(sourceCurrencyId);

        Currency targetCurrency = new Currency();
        targetCurrency.setId(targetCurrencyId);

        return exchangeRateService.getExchangeRate(sourceCurrency, targetCurrency);
    }
    @GetMapping("/getTargetCurrencies")
    @ResponseBody
    public List<Currency> getTargetCurrencies(@RequestParam Long sourceCurrencyId) {
        Currency sourceCurrency = new Currency();
        sourceCurrency.setId(sourceCurrencyId);
        return exchangeRateService.getTargetCurrencies(sourceCurrency);
    }
}
