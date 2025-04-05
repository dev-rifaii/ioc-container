package org.rifaii.components;

import org.rifaii.ioc.Component;

@Component
public class John {
    private final Doe doe;
    private final Foo foo;
    private final ImageRegistry imageRegistry;

    public John(Doe doe, Foo foo, ImageRegistry imageRegistry) {
        this.doe = doe;
        this.foo = foo;
        this.imageRegistry = imageRegistry;
    }
}
