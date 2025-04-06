package org.rifaii.cyclic.components;

import org.rifaii.ioc.Component;

@Component
public class C {
    private final E e;

    public C(E e) { this.e = e; }
}
