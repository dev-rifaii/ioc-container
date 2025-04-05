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

public class Container {

    private static final Logger log = LoggerFactory.getLogger(Container.class);

    private static Set<Class<?>> COMPONENTS;
    private static final Map<Class<?>, Object> REGISTRY = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Class<?>> INTERFACES_IMPL = new ConcurrentHashMap<>();
    private static Container INSTANCE;
    private static final Dag<Class<?>> dag = new Dag<>();

    private Container(Set<Class<?>> components) {
        COMPONENTS = components;
    }

    public static void withComponents(Set<Class<?>> components) throws InvocationTargetException,
                                                                       InstantiationException,
                                                                       IllegalAccessException {
        if (INSTANCE != null) {
            log.error("Container already initialized");
        }
        INSTANCE = new Container(components);
        registerComponents();
    }

    public static <T> T getComponent(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }
        if (!REGISTRY.containsKey(clazz)) {
            throw new RuntimeException("Class " + clazz.getName() + " is not registered");
        }

        return (T) REGISTRY.get(clazz);
    }

    static void registerComponents() {
        analyzeInterfacesImplementations();

        for (Class<?> component : COMPONENTS) {
            Constructor<?> constructorWithMostParams = Arrays.stream(component.getConstructors())
                .max(Comparator.comparing(Constructor::getParameterCount))
                .orElseThrow(() -> new RuntimeException("No constructor found"));

            for (Parameter param : constructorWithMostParams.getParameters()) {
                dag.addEdge(component, param.getType());
            }
        }

        dag.traverse();
    }

    private static void registerComponent(Class<?> component) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> constructorWithMostParams = Arrays.stream(component.getConstructors())
            .max(Comparator.comparing(Constructor::getParameterCount))
            .orElseThrow(() -> new RuntimeException("No constructor found"));

        if (constructorWithMostParams.getParameterCount() == 0) {
            try {
                REGISTRY.put(component, constructorWithMostParams.newInstance());
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        Parameter[] constructorParams = constructorWithMostParams.getParameters();

        List<Object> paramsInstances = new ArrayList<>();
        for (Parameter constructorParam : constructorParams) {
            Class<?> type = constructorParam.getType();
            if (REGISTRY.containsKey(type)) {
                paramsInstances.add(REGISTRY.get(type));
                continue;
            }
            else {
                boolean isInterface = type.isInterface();
                if (isInterface && INTERFACES_IMPL.get(type) == null) {
                    throw new RuntimeException("0 implementations found for " + type.getName());
                } else if (!isInterface && !type.isAnnotationPresent(Component.class)){
                    throw new RuntimeException("No component annotation found for " + type.getName());
                }

                registerComponent(isInterface ? INTERFACES_IMPL.get(type) : type);
            }

            paramsInstances.add(REGISTRY.get(type));
        }

        REGISTRY.put(
            component.getInterfaces().length > 0 ? component.getInterfaces()[0] : component,
            constructorWithMostParams.newInstance(paramsInstances.toArray())
        );
    }

    private static void analyzeInterfacesImplementations() {
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
