package org.rifaii.components;

import org.rifaii.ioc.Component;

import java.util.PriorityQueue;
import java.util.Queue;

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

