package dev.rits.ioc;

import java.util.Set;

public class Runner {

    public static Container run(Class<?> clazz) {
        try {
            Container container = new Container();
            Set<Class<?>> scannedComponents = container.scan(clazz.getPackage().getName());
            container.registerComponents(scannedComponents);
            return container;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
            throw new RuntimeException("Unreachable");
        }
    }

}
