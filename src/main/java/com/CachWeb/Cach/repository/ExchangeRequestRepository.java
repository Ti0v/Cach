package com.CachWeb.Cach.repository;

import com.CachWeb.Cach.entity.ExchangeRequest;
import com.CachWeb.Cach.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, Long> {
    Page<ExchangeRequest> findAllByArchived(boolean archived, Pageable pageable);

//    List<ExchangeRequest> findAllByArchived(boolean archived);

    @Query("SELECT u.email AS userEmail, COUNT(e) AS requestCount FROM ExchangeRequest e JOIN e.user u GROUP BY u.email")
    List<Object[]> findUsersAndRequestCounts();



}
