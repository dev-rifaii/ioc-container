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

    public void printAllRegisteredComponents() {
        REGISTRY.forEach((k, v) -> {
            System.out.println("Class: " + k.getName());
            System.out.println("Instance: " + v);
            System.out.println();
        });
    }

    public void registerComponents(Set<Class<?>> componentClasses) throws InvocationTargetException, InstantiationException, IllegalAccessException {
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
                if (REGISTRY.containsKey(type))
                    continue;
                else {
                    registerComponents(Set.of(type));
                }

                paramsInstances.add(REGISTRY.get(type));
            }

            constructorWithMostParams.newInstance(paramsInstances.toArray());

        }
    }

    public Set<Class<?>> scan(String rootPackageName) throws IOException, ClassNotFoundException {
        Set<File> rootPackageFiles = findRootPackageFiles(rootPackageName);
        Set<Class<?>> allClasses = search(rootPackageName, rootPackageFiles);
        Set<Class<?>> componentClasses = allClasses.stream()
            .filter(clazz -> clazz.isAnnotationPresent(Component.class))
            .collect(Collectors.toUnmodifiableSet());

        return componentClasses;
//        componentClasses.stream().forEach(componentClass -> {
//            Constructor<?> constructorWithMostParams = Arrays.stream(componentClass.getConstructors())
//                .max(Comparator.comparing(Constructor::getParameterCount))
//                .orElseThrow(() -> new RuntimeException("No constructor found"));
//
//            Arrays.stream(constructorWithMostParams.getParameters())
//                .forEach(param -> System.out.println(param.getType().getName()));
//
//        });
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
