# IoC-container
A simple IoC container implementation

## usage:
Annotate a class with `@Component`

```java
@Component
public class SomeComponent {
}
```

Use `Runner` class to initialize the container then access component using global container
```java
Container container = Runner.initializeContainer(Main.class);
container.getComponent(SomeComponent.class);
```

Benchmark:
```shell
./gradlew runBenchmark
```