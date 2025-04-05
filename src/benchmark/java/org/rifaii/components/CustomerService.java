package org.rifaii.components;

import org.rifaii.ioc.Component;

@Component
public class CustomerService {

    private final ImageRegistry imageRegistry;
    private final Bar bar;

    public CustomerService(ImageRegistry imageRegistry, Bar bar) {
        this.imageRegistry = imageRegistry;
        this.bar = bar;
    }

    public void registerCustomer() {
        System.out.println("Registering customer...");
    }

}

