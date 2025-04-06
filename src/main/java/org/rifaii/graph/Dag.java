package org.rifaii.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public class Dag<T> {
    private final Map<T, List<T>> MAP = new HashMap<>();
    private final Set<T> visited = new HashSet<>();

    public void addEdge(T from, T to) {
        List<T> toDependencies = MAP.get(to);

        if (toDependencies != null)
            for (T dependency : toDependencies) {
                if (dependency.equals(from))
                    throw new IllegalStateException("Cyclic dependency detected: " + from + " -> " + to);
            }

        MAP.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
    }

    public void traverse(BiConsumer<T, List<T>> consumer) {
        visited.clear();
        MAP.keySet().forEach(key -> traverse(key, consumer));
    }

    private void traverse(T key, BiConsumer<T, List<T>> consumer) {
        if (visited.contains(key)) {
            return;
        }

        List<T> dependencies = MAP.get(key) ;

        if (dependencies == null)
            dependencies = Collections.emptyList();

        for (int i = dependencies.size() - 1; i >= 0; i--) {
            T dep = dependencies.get(i);
            if (visited.contains(dep))
                continue;

            traverse(dep, consumer);
        }

        consumer.accept(key, dependencies);
        visited.add(key);
    }
}
