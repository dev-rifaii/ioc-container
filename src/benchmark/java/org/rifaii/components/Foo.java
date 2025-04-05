package org.rifaii.components;

import org.rifaii.ioc.Component;

@Component
public class Foo {

    private final Bar bar;

    public Foo(Bar bar) {
        this.bar = bar;
    }
}
