package dev.rits.ioc;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ComponentScanner {

    public void scan(String rootPackageName) throws IOException, ClassNotFoundException {
        Set<File> rootPackageFiles = findRootPackageFiles(rootPackageName);
        search(rootPackageName, rootPackageFiles);
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
                if (clazz.isAnnotationPresent(Component.class)) {
                    classes.add(clazz);
                    System.out.println("Found component: " + className);
                }
            }
        }
        return classes;
    }

    public Set<File> findRootPackageFiles(String rootPackageName) throws IOException {
        ClassLoader contextClassLoader = ClassLoader.getSystemClassLoader();
        Enumeration<URL> resources = contextClassLoader.getResources(rootPackageName.replaceAll("\\.", "/"));
        Set<File> rootPackageFiles = new HashSet<>();
        resources.asIterator()
            .forEachRemaining(url -> {
                rootPackageFiles.addAll(Set.of(new File(url.getFile()).listFiles()));
            });
        return rootPackageFiles;
    }
}
