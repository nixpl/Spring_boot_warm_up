package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer customer_id;
    private Short store_id;

    @Column(name = "first_name", length = 45)
    private String first_name;

    @Column(name = "last_name", length = 45)
    private String last_name;

    @Column(length = 45)
    private String email;

    @NotNull(message = "Address cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    private Address address;

    private Boolean activebool;

    private Date create_date;

    private Date last_update;

    private int active;
}
