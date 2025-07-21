package com.example.demo.service;

import com.example.demo.dto.customer.CustomerCreateDTO;
import com.example.demo.dto.customer.CustomerGetDTO;
import com.example.demo.exception.DataIntegrityViolationException;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.exception.UnknownFilterParameterException;
import com.example.demo.mapper.CustomerMapper;
import com.example.demo.model.Address;
import com.example.demo.model.Customer;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.testdata.DTOFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;


//    @BeforeEach
//    void setup() {}

    @Test
    void create_shouldCreateCustomer_whenEmailIsNotTakenAndAddressIdExists() {
        CustomerCreateDTO customerCreateDTO = DTOFactory.createDefaultCustomerCreateDto();
        Customer customer = DTOFactory.createDefaultCustomer(1);
        CustomerGetDTO customerGetDTO = DTOFactory.createDefaultCustomerGetDto(1);
        ResponseEntity<CustomerGetDTO> expectedResponse = ResponseEntity.status(HttpStatus.CREATED).body(customerGetDTO);
        Address existingAddress = DTOFactory.createDefaultAddress(1);

        when(customerMapper.toGetDTO(customer)).thenReturn(customerGetDTO);
        when(customerMapper.toEntity(customerCreateDTO)).thenReturn(customer);
        when(customerRepository.findByEmail(customerCreateDTO.email()))
                .thenReturn(Optional.empty());

        when(addressRepository.findById(customerCreateDTO.addressId())).thenReturn(Optional.of(existingAddress));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);


        ResponseEntity<CustomerGetDTO> response = customerService.create(customerCreateDTO);

        assertEquals(response, expectedResponse);
    }

    @Test
    void create_shouldThrowException_whenEmailIsNotTakenAndAddressIdNotExists() {
        CustomerCreateDTO customerCreateDTO = DTOFactory.createDefaultCustomerCreateDto();
        Customer customer = DTOFactory.createDefaultCustomer(1);

        when(customerMapper.toEntity(customerCreateDTO)).thenReturn(customer);
        when(customerRepository.findByEmail(customerCreateDTO.email()))
                .thenReturn(Optional.empty());

        when(addressRepository.findById(customerCreateDTO.addressId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> customerService.create(customerCreateDTO));
    }


    @Test
    void create_shouldThrowException_whenEmailIsAlreadyTaken() {
        CustomerCreateDTO customerCreateDTO = DTOFactory.createDefaultCustomerCreateDto();
        Customer existingCustomer = new Customer();

        when(customerRepository.findByEmail(customerCreateDTO.email()))
                .thenReturn(Optional.of(existingCustomer));

        assertThrows(DataIntegrityViolationException.class, () -> {
            customerService.create(customerCreateDTO);
        });
    }

    @Test
    void getAll_shouldThrowException_whenUnknowCustomerFilter(){

        Map<String,String> paramsMap = new HashMap<>();
        paramsMap.put("wrongKey", "1");
        Pageable pageable = PageRequest.of(0, 10);

        assertThrows(UnknownFilterParameterException.class, () -> {
            customerService.getAll(paramsMap, pageable);
        });

    }


    @Test
    void getAll_shouldReturnCustomers_whenCorrectFilters() {
        Integer customerId = 1;
        Customer expectedCustomer1 = DTOFactory.createDefaultCustomer(1);
        CustomerGetDTO expectedCustomerGetDto1 = DTOFactory.createDefaultCustomerGetDto(1);

        Customer expectedCustomer2 = DTOFactory.createDefaultCustomer(2);
        CustomerGetDTO expectedCustomerGetDto2 = DTOFactory.createDefaultCustomerGetDto(2);

        List<Customer> customerList = Arrays.asList(expectedCustomer1, expectedCustomer2);
        Page<Customer> customerPage = new PageImpl<>(customerList, PageRequest.of(0, customerList.size()), 20);


        when(customerRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(customerPage);
        when(customerMapper.toGetDTO(expectedCustomer1)).thenReturn(expectedCustomerGetDto1);
        when(customerMapper.toGetDTO(expectedCustomer2)).thenReturn(expectedCustomerGetDto2);

        Map<String, String> paramsMap = new HashMap<>();

        Pageable pageable = PageRequest.of(0, 10);
        Page<CustomerGetDTO> page = customerService.getAll(paramsMap, pageable);

        assertEquals(page, customerPage.map(customerMapper::toGetDTO));
    }
}