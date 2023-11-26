//package com.CachWeb.Cach.service;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import com.CachWeb.Cach.entity.Currency;
//import com.CachWeb.Cach.entity.ExchangeRequest;
//import com.CachWeb.Cach.repository.allRequestsRepository;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class AllRequestServices {
//    @Autowired
//     private allRequestsRepository repository;
//
//    @PersistenceContext
//    private EntityManager entityManager;
//    @Transactional
//    public void save(ExchangeRequest exchangeRequest) {
//        Currency sourceCurrency = entityManager.merge(exchangeRequest.getSourceCurrency());
//        Currency targetCurrency = entityManager.merge(exchangeRequest.getTargetCurrency());
//
//        exchangeRequest.setSourceCurrency(sourceCurrency);
//        exchangeRequest.setTargetCurrency(targetCurrency);
//
//        repository.save(exchangeRequest);
//    }
//    public List<ExchangeRequest> findAll(){
//        return repository.findAll();
//    }
//}
