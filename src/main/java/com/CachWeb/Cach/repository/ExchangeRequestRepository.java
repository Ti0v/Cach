package com.CachWeb.Cach.repository;

import com.CachWeb.Cach.entity.ExchangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Long> {
    List<ExchangeRequest> findAllByArchived(boolean archived);

}
