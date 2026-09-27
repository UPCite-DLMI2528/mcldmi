package com.connexal.mcdlmi.core.component;


import com.connexal.mcdlmi.MCDLMI;
import com.connexal.mcdlmi.api.component.ComponentId;
import com.connexal.mcdlmi.api.component.ComponentState;

import java.util.*;

public class ComponentGraph {
    private final Map<ComponentId, DefaultComponentDefinition> components;

    private ComponentGraph(Map<ComponentId, DefaultComponentDefinition> components) {
        this.components = components;
    }

    public static ComponentGraph of(Map<ComponentId, DefaultComponentDefinition> components) {
        return new ComponentGraph(components);
    }

    public void validate() {
        this.verifyExistence();
        this.verifyCycles();
        this.verifyCanEnable();
    }

    /**
     * Checks that all dependencies of all components exist in the given map.
     * If a dependency is missing, the component is marked as failed and an error is logged.
     */
    private void verifyExistence() {
        for (DefaultComponentDefinition component : this.components.values()) {
            if (component.disabled()) continue;

            // Check that all dependencies exist
            for (ComponentId dependency : component.info().dependencies()) {
                if (!this.components.containsKey(dependency)) {
                    MCDLMI.getLog().severe("Component \"" + component.info().id() + "\" has a missing dependency: \"" + dependency + "\"");
                    component.state(ComponentState.FAILED);
                }
            }
        }
    }

    /**
     * Checks for cycles in the dependency graph of the given components.
     * If a cycle is found, all components in the cycle are marked as failed and an error is logged.
     */
    private void verifyCycles() {
        record Frame(ComponentId id, Iterator<ComponentId> dependencies) {}

        Set<ComponentId> visited = new HashSet<>();

        for (DefaultComponentDefinition component : this.components.values()) {
            ComponentId root = component.info().id();
            if (component.disabled() || visited.contains(root)) continue;

            List<ComponentId> path = new ArrayList<>();
            Map<ComponentId, Integer> pathIndex = new HashMap<>();
            Deque<Frame> stack = new ArrayDeque<>();

            visited.add(root);
            pathIndex.put(root, 0);
            path.add(root);

            stack.push(new Frame(root, this.components.get(root).info().dependencies().iterator()));

            while (!stack.isEmpty()) {
                Frame frame = stack.peek();

                if (!frame.dependencies().hasNext()) {
                    // Finished this component; backtrack.
                    stack.pop();
                    pathIndex.remove(frame.id());
                    path.removeLast();
                    continue;
                }

                ComponentId dependency = frame.dependencies().next();

                // If the dependency is on the current DFS path, everything from it onwards forms the cycle.
                Integer cycleStart = pathIndex.get(dependency);

                if (cycleStart != null) {
                    MCDLMI.getLog().severe("Cycle detected in component dependencies involving: \"" + dependency + "\"");

                    // Mark all components in the cycle as failed
                    for (int i = cycleStart; i < path.size(); i++) {
                        this.components.get(path.get(i)).state(ComponentState.FAILED);
                    }

                    continue;
                }

                // Already visited elsewhere in the graph, so it cannot form a cycle with the current path.
                if (this.components.get(dependency) == null || !visited.add(dependency)) {
                    continue;
                }

                pathIndex.put(dependency, path.size());
                path.add(dependency);

                stack.push(new Frame(dependency, this.components.get(dependency).info().dependencies().iterator()));
            }
        }
    }

    /**
     * Checks that all non-failed components are dependent only on non-failed components.
     * If a component is dependent on a failed component, it is marked as failed and an error is logged.
     */
    private void verifyCanEnable() {
        boolean changed;
        do {
            changed = false;
            for (DefaultComponentDefinition component : this.components.values()) {
                if (component.disabled()) continue;

                for (ComponentId dependency : component.info().dependencies()) {
                    DefaultComponentDefinition dependencyComponent = this.components.get(dependency);
                    if (dependencyComponent.disabled()) {
                        MCDLMI.getLog().severe("Component \"" + component.info().id() + "\" cannot be enabled because its dependency \"" + dependency + "\" has failed");
                        component.state(ComponentState.FAILED);
                        changed = true;
                        break;
                    }
                }
            }
        } while (changed);
    }

    /**
     * Sorts the components in topological order based on their dependencies.
     * Components that have failed are ignored in the sorting process.
     * @return a list of components sorted in topological order, excluding failed components
     */
    public List<DefaultComponentDefinition> sorted() {
        Set<ComponentId> visited = new HashSet<>();
        List<DefaultComponentDefinition> sorted = new ArrayList<>();

        Deque<DefaultComponentDefinition> stack = new ArrayDeque<>(this.components.values());

        while (!stack.isEmpty()) {
            DefaultComponentDefinition component = stack.pop();
            visited.add(component.info().id());

            if (component.disabled()) continue;

            for (ComponentId dependency : component.info().dependencies()) {
                DefaultComponentDefinition dependencyComponent = this.components.get(dependency);
                if (!visited.contains(dependency)) {
                    stack.push(dependencyComponent);
                }
            }

            sorted.add(component);
        }

        return sorted;
    }
}
