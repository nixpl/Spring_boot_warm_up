package com.example.demo.controller;

import com.example.demo.dto.customer.CustomerGetDTO;
import com.example.demo.service.CustomerService;
import com.example.demo.testdata.DTOFactory;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    private final MockMvc mockMvc;

    private final CustomerService customerService;

    @InjectMocks
    private final CustomerController customerController;

    private final ObjectMapper objectMapper;

    @Autowired
    CustomerControllerTest(MockMvc mockMvc, CustomerService customerService, CustomerController customerController, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.customerService = customerService;
        this.customerController = customerController;
        this.objectMapper = objectMapper;
    }


    @TestConfiguration
    static class ControllerTestConfig {

        @Bean
        public CustomerService customerService() {
            return Mockito.mock(CustomerService.class);
        }
    }

    @Test
    void shouldReturnCustomerGetDTO_whenCustomerExists() throws Exception {
        Integer customerId = 1;
        CustomerGetDTO expectedCustomerGetDTO = DTOFactory.createDefaultCustomerGetDto(customerId);

        when(customerService.getById(customerId)).thenReturn(ResponseEntity.status(HttpStatus.OK).body(expectedCustomerGetDTO));

        String expectedCustomerGetDTOJson = objectMapper.writeValueAsString(expectedCustomerGetDTO);

        mockMvc.perform(get("/customer/" + customerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedCustomerGetDTOJson));
    }

    @Test
    void shouldReturnALLCustomers_whenCustomersExist() throws Exception {
        List<CustomerGetDTO> customerGetDTOList = Arrays.asList(DTOFactory.createDefaultCustomerGetDto(1), DTOFactory.createDefaultCustomerGetDto(2));
        Page<CustomerGetDTO> expectedPageCustomerGetDTO = new PageImpl<>(customerGetDTOList, PageRequest.of(0, customerGetDTOList.size()), 20);

        when(customerService.getAll(anyMap(), any(Pageable.class))).thenReturn(expectedPageCustomerGetDTO);

        String expectedPageCustomerGetDTOJson = objectMapper.writeValueAsString(expectedPageCustomerGetDTO);

        mockMvc.perform(get("/customer/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedPageCustomerGetDTOJson));
    }

    @Test
    void shouldReturnALLCustomersWithFilters_whenCustomersExist() throws Exception {
        List<CustomerGetDTO> customerGetDTOList = Arrays.asList(DTOFactory.createDefaultCustomerGetDto(1), DTOFactory.createDefaultCustomerGetDto(2));
        Page<CustomerGetDTO> expectedPageCustomerGetDTO = new PageImpl<>(customerGetDTOList, PageRequest.of(0, customerGetDTOList.size()), 20);

        when(customerService.getAll(argThat(
                map ->
                        "Poland".equals(map.get("search")) &&
                        "active".equals(map.get("status"))),
                any(Pageable.class))).thenReturn(expectedPageCustomerGetDTO);

        String expectedPageCustomerGetDTOJson = objectMapper.writeValueAsString(expectedPageCustomerGetDTO);

        mockMvc.perform(get("/customer/all?search=Poland&status=active"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedPageCustomerGetDTOJson));
    }

}
