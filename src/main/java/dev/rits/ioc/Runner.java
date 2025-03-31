package dev.rits.ioc;

import java.util.Set;

public class Runner {
    public static void run(Class<?> clazz) {
        try {
            Container container = new Container();
            Set<Class<?>> scannedComponents = container.scan("dev.rits");
            container.registerComponents(scannedComponents);
            container.printAllRegisteredComponents();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
