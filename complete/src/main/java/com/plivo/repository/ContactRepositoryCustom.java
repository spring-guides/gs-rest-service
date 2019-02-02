package com.plivo.repository;

import com.plivo.model.Contact;

import java.util.List;

/**
 * @author pradrd
 */
public interface ContactRepositoryCustom {

    Contact findByMobileNumber(String mobileNumber);

    Contact updateNameAndEmail(String id, String name, String email);

    List<Contact> findByNameAndEmail(String name, String email, int pageNo, int pageLimit);
}
