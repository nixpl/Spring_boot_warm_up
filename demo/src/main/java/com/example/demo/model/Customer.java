package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customer_id;

    private Long store_id;
    private String first_name;
    private String last_name;
    private String email;
    private Long address_id;
    private Boolean activebool;
    private Date create_date;
    private Date last_update;
//    active???
//

}
