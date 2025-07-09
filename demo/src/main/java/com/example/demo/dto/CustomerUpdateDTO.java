package com.example.demo.dto;

import com.example.demo.model.Address;
import jakarta.validation.constraints.*;

public record CustomerUpdateDTO(
                                 Short storeId,

                                 @Size(max = 45, message = "first_name cannot exceed 45 characters")
                                 String firstName,

                                 @Size(max = 45, message = "last_name cannot exceed 45 characters")
                                 String lastName,

                                 @Size(max = 45, message = "email cannot exceed 45 characters")
                                 @Email
                                 String email,

                                 Integer addressId,

                                 Integer active,

                                Boolean activebool) {
    @Override
    public String toString() {
        String censoredEmail = (email != null && email.contains("@")) ?
                email.replaceAll("(?<=.{3}).(?=[^@]*?@)", "*") :
                email;

        return "CustomerDTO[" +
                "storeId=" + storeId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + censoredEmail + '\'' +
                ", addressId=" + addressId +
                ", active=" + active +
                ']';
    }
}