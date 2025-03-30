package dev.rits.components;

import dev.rits.CustomerData;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    private final CustomerService customerService = new CustomerService();

    @Test
    void testRegistration() {
        CustomerData customerData = new CustomerData();
        customerData.email = "foo@bar.com";
        customerData.image = new File("somefile.jpg");
        customerService.registerCustomer(customerData);
    }
}