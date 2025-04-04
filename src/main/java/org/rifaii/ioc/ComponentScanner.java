package org.rifaii.ioc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ComponentScanner {

    private static final Logger log = LoggerFactory.getLogger(ComponentScanner.class);

    /**
     * Finds all classes in the given package that are annotated with @Component.
     * @param rootPackageName The root package name to search in.
     * @return Set of classes annotated with @Component
     */
    public static Set<Class<?>> findComponents(String rootPackageName) throws IOException, ClassNotFoundException {
        Set<File> rootPackageFiles = findRootPackageFiles(rootPackageName);
        Set<Class<?>> allClasses = findAllClasses(rootPackageName, rootPackageFiles);
        log.debug("Found {} classes in package {}", allClasses.size(), rootPackageName);

        return allClasses.stream()
            .filter(clazz -> clazz.isAnnotationPresent(Component.class))
            .collect(Collectors.toUnmodifiableSet());
    }

    private static Set<Class<?>> findAllClasses(String packageName, Set<File> files) throws ClassNotFoundException {
        Set<Class<?>> classes = new HashSet<>();
        for (File file : files) {
            if (file.isDirectory()) {
                log.trace("Looking for classes in directory: {}", file.getAbsolutePath());
                Set<Class<?>> subDirClasses = findAllClasses(
                    packageName.replaceAll("\\.", "/") + "/" + file.getName(),
                    Set.of(file.listFiles())
                );

                classes.addAll(subDirClasses);
                continue;
            }

            String fileName = file.getName();
            if (fileName.endsWith(".class")) {
                String className = packageName.replaceAll("/", ".") + "." + fileName.substring(0, fileName.length() - 6);
                log.trace("Found class: {}", className);
                Class<?> clazz = Class.forName(className);
                classes.add(clazz);
            }
        }

        return classes;
    }

    public static Set<File> findRootPackageFiles(String rootPackageName) throws IOException {
        log.debug("Looking for root package files in {}", rootPackageName);

        ClassLoader contextClassLoader = ClassLoader.getSystemClassLoader();
        Enumeration<URL> resources = contextClassLoader.getResources(rootPackageName.replaceAll("\\.", "/"));
        Set<File> rootPackageFiles = new HashSet<>();
        resources.asIterator().forEachRemaining(url -> rootPackageFiles.addAll(Set.of(new File(url.getFile()).listFiles())));

        log.trace("Finished looking for root package files in {}", rootPackageName);
        return rootPackageFiles;
    }

}
