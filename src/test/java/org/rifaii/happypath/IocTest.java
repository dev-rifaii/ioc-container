package org.rifaii.happypath;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.rifaii.ioc.Runner;

public class IocTest {

    @Test
    void happyPath() {
        Assertions.assertDoesNotThrow(() -> Runner.initializeContainer(IocTest.class));
    }
}
