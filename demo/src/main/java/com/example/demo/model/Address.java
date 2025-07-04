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
    private Integer address_id;

    @Column(length = 50, nullable = false)
    private String address;

    @Column(length = 50)
    private String address2;

    @Column(length = 20, nullable = false)
    private String district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;

    @Column(length = 10)
    private String postal_code;

    @Column(length = 20, nullable = false)
    private String phone;

    @Column(nullable = false)
    private Date last_update;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.last_update = new Date();
    }
}
