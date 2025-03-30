package dev.rits.components;

import dev.rits.CustomerData;

public class DataAccessObject {

    public Long store(CustomerData customerData) {
        System.out.println("Storing something...");
        return 1L;
    }
}
