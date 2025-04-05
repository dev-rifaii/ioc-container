package org.rifaii.ioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class Runner {

    private static final Logger log = LoggerFactory.getLogger(Runner.class);

    public static Container initializeContainer(Class<?> clazz) {
        try {
            Set<Class<?>> components = ComponentScanner.getComponents(clazz.getPackage().getName());
            return Container.withComponents(components);
        } catch (Exception e) {
            log.error("Error during container initialization: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
