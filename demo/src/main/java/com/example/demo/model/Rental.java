package com.example.demo.model;

import jakarta.persistence.*;
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
public class Rental {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rental_id")
    private Integer rentalId;

    @Column(name = "rental_date")
    private Date rentalDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", unique = true)
    private Inventory inventory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", unique = true)
    private Customer customer;

    @Column(name="return_date")
    private Date returnDate;

    @Column(name = "staff_id")
    private Integer staffId;

    @Column(name="last_update")
    private Date lastUpdate;
}
