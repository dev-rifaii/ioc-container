package org.rifaii.happypath.components;

import org.rifaii.ioc.Component;

@Component
public class E {
    private final F f;

    public E(F f) {
        this.f = f;
    }
}
