package dev.rits.components;

import java.io.File;

public class ImageRegistry {

    public void storeImage(File file) {
        System.out.println("Storing image " + file.getName());
    }
}
