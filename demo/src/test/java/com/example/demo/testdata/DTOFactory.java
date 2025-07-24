package com.example.demo.testdata;

import com.example.demo.dto.address.AddressCreateDTO;
import com.example.demo.dto.address.AddressGetDTO;
import com.example.demo.dto.address.AddressUpdateDTO;
import com.example.demo.dto.city.CityCreateDTO;
import com.example.demo.dto.city.CityGetDTO;
import com.example.demo.dto.city.CityUpdateDTO;
import com.example.demo.dto.country.CountryCreateDTO;
import com.example.demo.dto.country.CountryGetDTO;
import com.example.demo.dto.country.CountryUpdateDTO;
import com.example.demo.dto.customer.CustomerCreateDTO;
import com.example.demo.dto.customer.CustomerGetDTO;
import com.example.demo.dto.customer.CustomerUpdateDTO;
import com.example.demo.model.*;

public final class DTOFactory {

    private  DTOFactory() {};

    public static Country createDefaultCountry(Integer countryId){
        return new Country(countryId, "Polska", null);
    }

    public static CountryGetDTO createDefaultCountryGetDto(Integer countryId) {
        return new CountryGetDTO(countryId,
                "Polska");
    }
    public static CountryCreateDTO createDefaultCountryGetDto() {
        return new CountryCreateDTO(
                "Polska");
    }
    public static CountryUpdateDTO createDefaultCountryUpdateDto() {
        return new CountryUpdateDTO(
                "Polska");
    }



    public static City createDefaultCity(Integer cityId){
        return new City(cityId, "Krakow", createDefaultCountry(1), null);
    }

    public static CityGetDTO createDefaultCityGetDto(Integer cityId) {
        return new CityGetDTO(cityId,
                "Krakow",
                createDefaultCountryGetDto(1));
    }
    public static CityCreateDTO createDefaultCityCreateDto() {
        return new CityCreateDTO(
                "Krakow",
                1);
    }
    public static CityUpdateDTO createDefaultCityUpdateDto() {
        return new CityUpdateDTO(
                "Krakow",
                1);
    }




    public static Address createDefaultAddress(Integer addressId) {
        return new Address(addressId, "Sezamkowa", "8", "Krowodrza", createDefaultCity(1), "30-300", "000-000-000", null);
    }

    public static AddressGetDTO createDefaultAddressGetDto(Integer addressId) {
        return new AddressGetDTO(addressId,
                "Sezamkowa",
                "8",
                "Krowodrza",
                createDefaultCityGetDto(1),
                "30-300",
                "000-000-000");
    }
    public static AddressCreateDTO createDefaultAddressCreateDto() {
        return new AddressCreateDTO(
                "Sezamkowa",
                "8",
                "Krowodrza",
                1,
                "30-300",
                "000-000-000");
    }
    public static AddressUpdateDTO createDefaultAddressUpdateDto() {
        return new AddressUpdateDTO(
                "Czekoladowa",
                null,
                null,
                null,
                null,
                null);
    }


    public static Customer createDefaultCustomer(Integer customerId) {
        return new Customer(customerId, (short)1, "Jan", "Kowalski", "jan.kowalski@gmail.com", createDefaultAddress(1), true, null, null, 1, Gender.MALE);
    }

    public static CustomerGetDTO createDefaultCustomerGetDto(Integer customerId) {
        return new CustomerGetDTO(customerId,
                "Jan",
                "Kowalski",
                Gender.MALE,
                "jan.kowalski@gmail.com",
                createDefaultAddressGetDto(1),
                true);
    }
    public static CustomerCreateDTO createDefaultCustomerCreateDto() {
        return new CustomerCreateDTO( (short)1,
                "Jan",
                "Kowalski",
                "jan.kowalski@gmail.com",
                1,
                1);
    }
    public static CustomerUpdateDTO createDefaultCustomerUpdateDto() {
        return new CustomerUpdateDTO( null,
                "Marek",
                null,
                null,
                "marek.kowalski@gmail.com",
                1,
                null,
                null);
    }

    public static CustomerUpdateDTO createDefaultCustomerUpdateWithBadActiveValueDto() {
        return new CustomerUpdateDTO( null,
                "Marek",
                null,
                null,
                "marek.kowalski@gmail.com",
                1,
                -1,
                null);
    }

    public static CustomerGetDTO createDefaultCustomerUpdatedGetDto(Integer customerId) {
        return new CustomerGetDTO(customerId,
                "Marek",
                "Kowalski",
                Gender.MALE,
                "marek.kowalski@gmail.com",
                createDefaultAddressGetDto(1),
                true);
    }


}
