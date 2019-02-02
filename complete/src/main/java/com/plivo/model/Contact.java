package com.plivo.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author pradrd
 */
@Data
@Document
public class Contact {

    @Id
    private String mobileNumber;

    private String email;

    private String name;
}
