package com.CachWeb.Cach.repository;

import com.CachWeb.Cach.entity.ContactForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<ContactForm,Long > {
}
