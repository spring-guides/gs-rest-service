package com.plivo.controller;

import com.plivo.model.Contact;
import com.plivo.repository.ContactRepository;
import com.plivo.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/contact")
public class ContactController {


    @Autowired
    ContactService contactService;

    @Autowired
    ContactRepository contactRepository;

    public static final Logger logger = LoggerFactory.getLogger(ContactController.class);

    @RequestMapping("/health")
    public String health(){
        return "is Up";
    }

    @RequestMapping(value = "/all" , method = RequestMethod.GET)
    public List<Contact> getAllContacts(){

        return contactService.getContacts();

    }

    @RequestMapping(value = "" , method = RequestMethod.GET)
    public Contact getContact(@RequestParam("mobileNumber") String mobileNumber){

        Contact contact = contactService.getContactByMobileNumber(mobileNumber);

        return contact;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Contact saveContact(@RequestBody Contact contact){

        Contact contact1 = contactService.save(contact);

        logger.info("contact : {} saved ",contact1);
        return contact1;
    }

    @RequestMapping(value = "" , method = RequestMethod.PUT)
    public ResponseEntity<Contact> editContact(@RequestParam(value = "mobileNumber", required = true) String mobileNumber,
                                               @RequestParam(value = "name", required = false) String name ,
                                               @RequestParam(value = "email", required = false) String email){

        if(StringUtils.isEmpty(name) && StringUtils.isEmpty(email)){
            return new ResponseEntity<Contact>(HttpStatus.BAD_REQUEST);
        }

        Contact contact = contactService.getContactByMobileNumber(mobileNumber);
        if(contact == null) {
            logger.info("Mobile Number doesnt exists");
            return new ResponseEntity<Contact>(HttpStatus.BAD_REQUEST);
        }

        contact = contactService.updateNameAndEmail(mobileNumber, name, email);

        return new ResponseEntity<>(contact, HttpStatus.OK);
    }

    @RequestMapping(value = "" , method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteContact(@RequestParam(value = "mobileNumber") String mobileNumber){

        if(StringUtils.isEmpty(mobileNumber)){
            return new ResponseEntity<String>("empty MobileNumber", HttpStatus.BAD_REQUEST);
        }

        Contact contact = contactService.getContactByMobileNumber(mobileNumber);
        if(contact == null){
            logger.info("Mobile Number doesnt exists");
            return new ResponseEntity<String>("Mobile Number doesnt exists",HttpStatus.BAD_REQUEST);
        }

        contactService.delete(mobileNumber);
        return new ResponseEntity<>("Delete success", HttpStatus.OK);
    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public List<Contact> searchByNameAndEmail(@RequestParam(value = "name", required = false) String name ,
                                              @RequestParam(value = "email", required = false) String email,
                                              @RequestParam(value = "page" , required = false, defaultValue = "0") String pageNumber){

        if(StringUtils.isEmpty(name) && StringUtils.isEmpty(email)){
            logger.info("Empty name and email");
            return contactService.getContacts();
        }

        List<Contact> contacts = contactService.searchByNameAndEmail(name, email, pageNumber);

        return contacts;
    }

}
