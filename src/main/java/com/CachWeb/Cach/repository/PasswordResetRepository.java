package com.CachWeb.Cach.repository;

import com.CachWeb.Cach.entity.PasswordReset;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {


    PasswordReset findByToken(String token);

    boolean existsByUserId(Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM PasswordReset pr WHERE pr.user.id = ?1")
    void deleteByUserId(Long userId);

}
