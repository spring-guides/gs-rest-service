package com.plivo.repository.Impl;

import com.plivo.model.Contact;
import com.plivo.repository.ContactRepositoryCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pradrd
 */
public class ContactRepositoryCustomImpl implements ContactRepositoryCustom {

    @Autowired
    MongoOperations mongoOperations;

    public static final Logger logger = LoggerFactory.getLogger(ContactRepositoryCustomImpl.class);

    @Override
    public Contact findByMobileNumber(String mobileNumber) {

        Criteria criteria = Criteria.where("_id").is(mobileNumber);
        Query query = new Query(criteria);
        logger.info("findByMobile query : {} ", query);
        Contact contact = mongoOperations.findOne(query, Contact.class ,"contact");
        return contact;

    }

    @Override
    public Contact updateNameAndEmail(String id, String name, String email) {

        Criteria criteria = Criteria.where("_id").is(id);
        Update update = new Update();
        if(!StringUtils.isEmpty(name)){
            update.set("name", name);
        }
        if(!StringUtils.isEmpty(email)){
            update.set("email", email);
        }

        Query query = new Query(criteria);

        logger.info("update Name and Email Query : {} ", query);
        Contact contact = mongoOperations.findAndModify(query, update, FindAndModifyOptions.options().returnNew(true), Contact.class);
        logger.info("Id : {} updated with name : {} and email : {} contact :  {} ", id, name, email, contact);
        return contact;
    }

    @Override
    public List<Contact> findByNameAndEmail(String name, String email, int pageNo, int pageLimit) {

        List<Criteria> criteria = new ArrayList<>();
        if(!StringUtils.isEmpty(name)){
            criteria.add(Criteria.where("name").is(name));
        }
        if(!StringUtils.isEmpty(email)){
            criteria.add(Criteria.where("email").is(email));
        }

        Query query = new Query();
        query.addCriteria((new Criteria()).andOperator((Criteria[])criteria.toArray(new Criteria[criteria.size()])));
        query.skip(pageNo);
        query.limit(pageLimit);
        logger.info("findByName and email query : {} ", query);
        List<Contact> contacts = mongoOperations.find(query, Contact.class, "contact");

        logger.info("Contacts found with name : {} email : {} is : {} ", name, email, contacts);
        return contacts;
    }


}
