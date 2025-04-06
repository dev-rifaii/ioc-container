package org.rifaii.cyclic.components;

import org.rifaii.ioc.Component;

@Component
public class G {
    private final D d;

    public G(D d) { this.d = d; }
}
