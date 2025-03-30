package dev.rits.components;

import dev.rits.CustomerData;

public class CustomerService {

    private EmailSender emailSender = new EmailSender();
    private ImageRegistry imageRegistry = new ImageRegistry();
    private DataAccessObject dataAccessObject = new DataAccessObject();

    public void registerCustomer(CustomerData customerData) {
        imageRegistry.storeImage(customerData.image);
        emailSender.sendEmail(customerData.email, "Foo bar");
        dataAccessObject.store(customerData);
    }

}
