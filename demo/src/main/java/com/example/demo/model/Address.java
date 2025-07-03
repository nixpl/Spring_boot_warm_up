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
    @Column(name = "address_id")
    private Integer addressId;

    @Column(name = "address", length = 50, nullable = false)
    private String address;

    @Column(name = "address2", length = 50)
    private String address2;

    @Column(name = "district", length = 20, nullable = false)
    private String district;

    @Column(name = "city_id", nullable = false)
    private Short cityId;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(name = "phone", length = 20, nullable = false)
    private String phone;

    @Column(name = "last_update", nullable = false)
    private Date lastUpdate;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.lastUpdate = new Date();
    }
}
