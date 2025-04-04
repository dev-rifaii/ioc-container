package org.rifaii.ioc;

import java.util.Set;

class ComponentInfo {

    private Class<?> classType;
    private Set<Class<?>> interfacesImplemented;

    ComponentInfo(Class<?> classType, Set<Class<?>> interfacesImplemented) {
        this.classType = classType;
        this.interfacesImplemented = interfacesImplemented;
    }
}
