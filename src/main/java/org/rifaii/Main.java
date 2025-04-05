package org.rifaii;

import org.rifaii.ioc.Container;
import org.rifaii.ioc.Runner;

public class Main {

    public static void main(String[] args) {
        Runner.initializeContainer(Main.class);
    }
}