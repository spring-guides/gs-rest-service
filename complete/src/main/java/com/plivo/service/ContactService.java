package com.plivo.service;

import com.plivo.constant.ContactConstants;
import com.plivo.controller.ContactController;
import com.plivo.model.Contact;
import com.plivo.repository.ContactRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pradrd
 */
@Service
public class ContactService {

    @Autowired
    ContactRepository contactRepository;

    public static final Logger logger = LoggerFactory.getLogger(ContactService.class);

    public List<Contact> getContacts(){
        List<Contact> contactList = contactRepository.findAll();

        return contactList;
    }

    public Contact getContactByMobileNumber(String id){

        Contact contact = contactRepository.findByMobileNumber(id);
        logger.info("MobileNumber : {} has contact info : {} ", id, contact);

        return contact;
    }


    public Contact save(Contact contact) {
        Contact contact1 = contactRepository.save(contact);
        return contact1;
    }

    public Contact updateNameAndEmail(String id, String name, String email){

        Contact contact = contactRepository.updateNameAndEmail(id, name, email);
        return contact;
    }


    public void delete(String mobileNumber) {
        contactRepository.deleteById(mobileNumber);
        logger.info("Contact associated with MobileNumber : {} deleted" , mobileNumber);

    }

    public List<Contact> searchByNameAndEmail(String name, String email, String pageNumber) {

        int pageLimit = ContactConstants.PAGE_LIMIT;
        int pageNo;

        try{
            pageNo = Integer.parseInt(pageNumber);
        }
        catch (Exception e){
            logger.error("Exception wrt PageNumber : {} e - {} ", pageNumber, e);
            pageNo = 0;
        }

        return contactRepository.findByNameAndEmail(name, email, pageNo, pageLimit);
    }
}
