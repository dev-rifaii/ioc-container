package dev.rits.components;

import dev.rits.CustomerData;
import dev.rits.ioc.Component;

@Component
public class CustomerService {

    private final ImageRegistry imageRegistry;

    public CustomerService(ImageRegistry imageRegistry) {
        this.imageRegistry = imageRegistry;
    }

    public void registerCustomer() {
        System.out.println("Registering customer...");
    }

}
