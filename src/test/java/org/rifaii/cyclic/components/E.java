package org.rifaii.cyclic.components;

import org.rifaii.ioc.Component;

@Component
public class E {
    private final B b;
    private final F f;

    public E(B b, F f) {
        this.b = b;
        this.f = f; }
}
