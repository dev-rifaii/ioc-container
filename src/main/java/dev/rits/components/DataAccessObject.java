package dev.rits.components;

import dev.rits.CustomerData;
import dev.rits.ioc.Component;

@Component
public class DataAccessObject {

    public DataAccessObject() {
        System.out.println("DataAccessObject created");
    }

    public DataAccessObject(CustomerService test) {
        System.out.println("DataAccessObject created");
    }

    public Long store(CustomerData customerData) {
        System.out.println("Storing something...");
        return 1L;
    }
}
