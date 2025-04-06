package org.rifaii.cyclic.components;

import org.rifaii.ioc.Component;

@Component
public class A {

    private final B b;

    public A(B b) { this.b = b; }
}
