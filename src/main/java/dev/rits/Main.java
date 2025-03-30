package dev.rits;

import dev.rits.ioc.Component;
import dev.rits.ioc.ComponentScanner;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ComponentScanner componentScanner = new ComponentScanner();
        componentScanner.scan("dev.rits");
    }

}