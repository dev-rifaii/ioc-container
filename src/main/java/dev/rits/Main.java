package dev.rits;

import dev.rits.components.ImageRegistry;
import dev.rits.ioc.Container;
import dev.rits.ioc.Runner;

public class Main {

    public static void main(String[] args) {
        Container container = Runner.run(Main.class);

        ImageRegistry imageRegistry = container.getComponent(ImageRegistry.class);
    }
}