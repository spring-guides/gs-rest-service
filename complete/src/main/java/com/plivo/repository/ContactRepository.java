package com.plivo.repository;

import com.plivo.model.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author pradrd
 */
@Repository
public interface ContactRepository extends MongoRepository<Contact, String>, ContactRepositoryCustom {


}
