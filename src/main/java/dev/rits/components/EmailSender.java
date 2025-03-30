package dev.rits.components;

public class EmailSender {

    private EmailServiceClient emailServiceClient = new EmailServiceClient();

    public void sendEmail(String to, String text) {
        System.out.println("Sending email to " + to);
    }
}
