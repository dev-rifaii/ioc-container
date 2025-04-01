package dev.rits.ioc;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Container {

    private final Map<Class<?>, Object> REGISTRY = new ConcurrentHashMap<>();

    public <T> T getComponent(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }
        if (!REGISTRY.containsKey(clazz)) {
            throw new RuntimeException("Class " + clazz.getName() + " is not registered");
        }

        return (T) REGISTRY.get(clazz);
    }

    void registerComponents(Set<Class<?>> componentClasses) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        for (Class<?> componentClass : componentClasses) {
            if (REGISTRY.containsKey(componentClass))
                continue;

            Constructor<?> constructorWithMostParams = Arrays.stream(componentClass.getConstructors())
                .max(Comparator.comparing(Constructor::getParameterCount))
                .orElseThrow(() -> new RuntimeException("No constructor found"));

            if (constructorWithMostParams.getParameterCount() == 0) {
                try {
                    REGISTRY.put(componentClass, constructorWithMostParams.newInstance());
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                continue;
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
                    if (!type.isAnnotationPresent(Component.class)) {
                        System.out.println("ERROR");
                        System.exit(1);
                    }
                    registerComponents(Set.of(type));
                }

                paramsInstances.add(REGISTRY.get(type));
            }

            REGISTRY.put(componentClass, constructorWithMostParams.newInstance(paramsInstances.toArray()));
        }
    }

    public Set<Class<?>> scan(String rootPackageName) throws IOException, ClassNotFoundException {
        Set<File> rootPackageFiles = findRootPackageFiles(rootPackageName);
        Set<Class<?>> allClasses = search(rootPackageName, rootPackageFiles);
        Set<Class<?>> componentClasses = allClasses.stream()
            .filter(clazz -> clazz.isAnnotationPresent(Component.class))
            .collect(Collectors.toUnmodifiableSet());

        return componentClasses;
    }

    public static Set<Class<?>> search(String packageName, Set<File> files) throws ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<>();
        for (File file : files) {
            if (file.isDirectory()) {
                Set<Class<?>> subDirClasses = search(packageName.replaceAll("\\.", "/") + "/" + file.getName(), Set.of(file.listFiles()));
                classes.addAll(subDirClasses);
                continue;
            }

            String fileName = file.getName();
            if (fileName.endsWith(".class")) {
                String className = packageName.replaceAll("/", ".") + "." + fileName.substring(0, fileName.length() - 6);
                System.out.println("Found class: " + className);
                Class<?> clazz = Class.forName(className);
                classes.add(clazz);
            }
        }
        return classes;
    }

    public Set<File> findRootPackageFiles(String rootPackageName) throws IOException {
        ClassLoader contextClassLoader = ClassLoader.getSystemClassLoader();
        Enumeration<URL> resources = contextClassLoader.getResources(rootPackageName.replaceAll("\\.", "/"));
        Set<File> rootPackageFiles = new HashSet<>();
        resources.asIterator()
            .forEachRemaining(url -> rootPackageFiles.addAll(Set.of(new File(url.getFile()).listFiles())));
        return rootPackageFiles;
    }
}
