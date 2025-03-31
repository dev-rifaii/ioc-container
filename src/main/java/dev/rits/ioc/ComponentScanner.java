package dev.rits.ioc;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ComponentScanner {

    public void scan(String rootPackageName) throws IOException, ClassNotFoundException {
        Set<File> rootPackageFiles = findRootPackageFiles(rootPackageName);
        Set<Class<?>> allClasses = search(rootPackageName, rootPackageFiles);
        Set<Class<?>> componentClasses = allClasses.stream()
            .filter(clazz -> clazz.isAnnotationPresent(Component.class))
            .collect(Collectors.toUnmodifiableSet());
        System.out.println("Constructors:");
        componentClasses.stream().forEach(componentClass -> {
            Optional<Constructor<?>> firstFound = Arrays.stream(componentClass.getConstructors())
                .max(Comparator.comparing(Constructor::getParameterCount));

            System.out.println(firstFound.get());
        });
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
