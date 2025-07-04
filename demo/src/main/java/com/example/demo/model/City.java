package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer city_id;

    @NotBlank(message = "City cannot be empty")
    @Column(length = 50)
    private String city;

    @NotNull(message = "Country_id cannot be null")
    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;
    private Date last_update;
}
