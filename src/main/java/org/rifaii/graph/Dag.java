package org.rifaii.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Dag<T> {
    private final Map<T, List<T>> MAP = new HashMap<>();
    private final Set<T> visited = new HashSet<>();

    public void addEdge(T from, T to) {
        MAP.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
    }

    public void traverse() {
        MAP.keySet().forEach(this::traverse);
        visited.clear();
    }

    public void traverse(T key) {
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

            traverse(dep);
        }

        System.out.println(key);
        visited.add(key);
    }

    public void printGraph() {
        System.out.println("Directed Acyclic Graph:");
        for (Map.Entry<T, List<T>> entry : MAP.entrySet()) {
            T from = entry.getKey();
            List<T> toList = entry.getValue();
            System.out.printf("  %s -> %s%n", from, toList.isEmpty() ? "∅" : String.join(", ", toList.stream().map(String::valueOf).toList()));
        }
    }
}
