package dev.rits.components;

import dev.rits.ioc.Component;

import java.io.File;

@Component
public class ImageRegistry {

    public void storeImage(File file) {
        System.out.println("Storing image " + file.getName());
    }
}
