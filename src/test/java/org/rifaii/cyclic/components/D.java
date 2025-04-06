package org.rifaii.cyclic.components;

import org.rifaii.ioc.Component;

@Component
public class D {
    private final E e;

    public D(E e) { this.e = e; }
}
