package org.rifaii.cyclic;

import org.junit.jupiter.api.Test;
import org.rifaii.ioc.Runner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IocTest {

    @Test
    void cyclicDependency_Between_B_and_E() {
        Exception thrown = assertThrows(Exception.class, () -> Runner.initializeContainer(IocTest.class));

        assertEquals(
            "java.lang.IllegalStateException: Cyclic dependency detected: class org.rifaii.cyclic.components.E -> class org.rifaii.cyclic.components.B",
            thrown.getMessage()
        );
    }
}
