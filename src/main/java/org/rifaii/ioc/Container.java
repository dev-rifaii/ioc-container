package org.rifaii.ioc;

import org.rifaii.graph.Dag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class Container {

    private static final Logger log = LoggerFactory.getLogger(Container.class);

    private Set<Class<?>> COMPONENTS;
    private final Map<Class<?>, Object> REGISTRY = new ConcurrentHashMap<>();
    private final Map<Class<?>, Class<?>> INTERFACES_IMPL = new ConcurrentHashMap<>();
    private final Dag<Class<?>> dag = new Dag<>();

    private Container(Set<Class<?>> components) {
        COMPONENTS = components;
        registerComponents();
    }

    public static Container withComponents(Set<Class<?>> components) {
        Container container = new Container(components);
        return container;
    }

    @SuppressWarnings("unchecked")
    public <T> T getComponent(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }
        if (!REGISTRY.containsKey(clazz)) {
            throw new RuntimeException("Class " + clazz.getName() + " is not registered");
        }

        return (T) REGISTRY.get(clazz);
    }

    /**
     * Interfaces not supported yet
     */
    void registerComponents() {
//        analyzeInterfacesImplementations();

        Function<Class<?>, Constructor<?>> constructorFinder = component -> Arrays.stream(component.getConstructors())
            .max(Comparator.comparing(Constructor::getParameterCount))
            .orElseThrow(() -> new RuntimeException("No constructor found"));

        for (Class<?> component : COMPONENTS) {
            Constructor<?> constructorWithMostParams = constructorFinder.apply(component);

            for (Parameter param : constructorWithMostParams.getParameters()) {
                dag.addEdge(component, param.getType());
            }
        }
        dag.traverse((clazz, params) -> {
            try {
                REGISTRY.put(clazz, constructorFinder.apply(clazz).newInstance(params.stream().map(REGISTRY::get).toArray()));
                log.debug("Registered component: {}", clazz.getName());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void analyzeInterfacesImplementations() {
        COMPONENTS.forEach(component -> {
            for (Class<?> anInterface : component.getInterfaces()) {
                if (INTERFACES_IMPL.containsKey(anInterface)) {
                    throw new RuntimeException("Interface implemented multiple times");
                } else {
                    INTERFACES_IMPL.put(anInterface, component);
                }
            }
        });
    }
}
