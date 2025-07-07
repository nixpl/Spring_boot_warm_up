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
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="address_id")
    private Integer addressId;

    @Column(length = 50, nullable = false)
    private String address;

    @Column(length = 50)
    private String address2;

    @Column(length = 20, nullable = false)
    private String district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(length = 20, nullable = false)
    private String phone;

    @Column(name ="last_update", nullable = false)
    private Date lastUpdate;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.lastUpdate = new Date();
    }
}
