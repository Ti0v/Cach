package com.CachWeb.Cach.service;
import com.CachWeb.Cach.email.EmailService;
import com.CachWeb.Cach.entity.Currency;

import com.CachWeb.Cach.entity.ExchangeRequest;
import com.CachWeb.Cach.repository.ExchangeRequestRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
public class ExchangeRequestService {

@Autowired
   private EmailService emailService;
    @Autowired
    private ExchangeRequestRepository exchangeRequestRepository;

    @Autowired
    private ExchangeRateService exchangeRateService;
    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public void initiateExchange(ExchangeRequest exchangeRequest) {
        Currency sourceCurrency = exchangeRequest.getSourceCurrency();
        Currency targetCurrency = exchangeRequest.getTargetCurrency();

        BigDecimal exchangeRate = exchangeRateService.getExchangeRate(sourceCurrency, targetCurrency);

        BigDecimal receivingAmount = calculateReceivingAmount(exchangeRequest.getSendingAmount(), exchangeRate);
        exchangeRequest.setReceivingAmount(receivingAmount);
        exchangeRequestRepository.save(exchangeRequest);
    }

    public List<ExchangeRequest> findAll(){
       return exchangeRequestRepository.findAll();
    }


    public List<ExchangeRequest> getAllRequestsIncludingArchived() {
        return exchangeRequestRepository.findAllByArchived(true);
    }
    public List<ExchangeRequest> getAllRequestsWithoutArchived() {
        return exchangeRequestRepository.findAllByArchived(false);
    }


    public BigDecimal calculateReceivingAmount(BigDecimal sendingAmount, BigDecimal exchangeRate) {

        if (sendingAmount == null || exchangeRate == null) {
            throw new IllegalArgumentException("Invalid input for calculation");
        }

        return sendingAmount.multiply(exchangeRate);
    }
    @Transactional
    public void Save(ExchangeRequest exchangeRequest) {
        Currency sourceCurrency = entityManager.merge(exchangeRequest.getSourceCurrency());
        Currency targetCurrency = entityManager.merge(exchangeRequest.getTargetCurrency());

        exchangeRequest.setSourceCurrency(sourceCurrency);
        exchangeRequest.setTargetCurrency(targetCurrency);
     //  emailService.sendEmail("uae70008@gmail.com","New Record ","New Record On owr System");


        exchangeRequestRepository.save(exchangeRequest);
    }


    public void remove(Long id) {
        exchangeRequestRepository.deleteById(id);
    }



    public void archiveRequest(Long requestId) {
        Optional<ExchangeRequest> optionalRequest = exchangeRequestRepository.findById(requestId);
        optionalRequest.ifPresent(request -> {
            request.setArchived(true);
            exchangeRequestRepository.save(request);
        });
    }


    public void updateRequest(Long requestId, BigDecimal sendingAmount, BigDecimal receivingAmount, String walletNumber) {
        // Retrieve the exchange request by ID
        Optional<ExchangeRequest> optionalRequest = exchangeRequestRepository.findById(requestId);

        // Check if the request exists
        if (optionalRequest.isPresent()) {
            ExchangeRequest exchangeRequest = optionalRequest.get();

            // Update the fields
            exchangeRequest.setSendingAmount(sendingAmount);
            exchangeRequest.setReceivingAmount(receivingAmount);
            exchangeRequest.getWallet().setWalletNumber(walletNumber);


            // Save the updated request

            exchangeRequestRepository.save(exchangeRequest);
        } else {

        }
    }
}



