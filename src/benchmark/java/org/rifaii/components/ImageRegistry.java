package org.rifaii.components;

import org.rifaii.ioc.Component;

import java.io.File;

@Component
public class ImageRegistry {

    public void storeImage(File file) {
        System.out.println("Storing image " + file.getName());
    }
}
