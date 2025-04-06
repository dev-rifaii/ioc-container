package org.rifaii.cyclic.components;

import org.rifaii.ioc.Component;

@Component
public class B {
    private final C c;
    private final D d;
    private final E e;

    public B(C c, D d, E e) {
        this.c = c;
        this.d = d;
        this.e = e;
    }
}
