package com.CachWeb.Cach.service;

import com.CachWeb.Cach.entity.ContactForm;
import com.CachWeb.Cach.repository.ContactRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    private ContactRepository contactRepository;
    public ContactService(ContactRepository contactRepository){
        this.contactRepository = contactRepository;
    }

    public List<ContactForm> getAllContacts() {
        return contactRepository.findAll();
    }

    @Transactional
    public void addContact(ContactForm contactForm) {

        contactRepository.save(contactForm);
    }
    @Transactional
    public void remove(Long id) {
        contactRepository.deleteById(id);
    }

}
