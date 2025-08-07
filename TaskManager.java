package com.taskmanager;

import java.util.*;

public class TaskManager {
    private final Map<String, Task> tasks = new HashMap<>();
    private final Map<String, List<String>> dependencies = new HashMap<>();

    // Add a task to the manager
    public void addTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            throw new IllegalArgumentException("Task with ID " + task.getId() + " already exists");
        }
        tasks.put(task.getId(), task);
        dependencies.putIfAbsent(task.getId(), new ArrayList<>());
    }

    // Add a dependency between tasks
    public void addDependency(String taskId, String dependsOnId) {
        if (!tasks.containsKey(taskId)) {
            throw new IllegalArgumentException("Task " + taskId + " does not exist");
        }
        if (!tasks.containsKey(dependsOnId)) {
            throw new IllegalArgumentException("Dependency " + dependsOnId + " does not exist");
        }
        if (taskId.equals(dependsOnId)) {
            throw new IllegalArgumentException("Task cannot depend on itself");
        }
        dependencies.computeIfAbsent(taskId, k -> new ArrayList<>()).add(dependsOnId);
    }

    // Schedule tasks in a valid order
    public List<Task> scheduleTasks() {
        List<Task> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> inProgress = new HashSet<>();
        List<String> sortedIds = new ArrayList<>();

        // Perform topological sort
        for (String taskId : tasks.keySet()) {
            if (!visited.contains(taskId)) {
                topologicalSort(taskId, visited, inProgress, sortedIds);
            }
        }

        // Convert sorted IDs to tasks
        for (String id : sortedIds) {
            result.add(tasks.get(id));
        }
        return result;
    }

    private void topologicalSort(String taskId, Set<String> visited, Set<String> inProgress, List<String> sortedIds) {
        if (inProgress.contains(taskId)) {
            throw new IllegalStateException("Cyclic dependency detected involving task " + taskId);
        }
        if (!visited.contains(taskId)) {
            inProgress.add(taskId);
            List<String> deps = dependencies.getOrDefault(taskId, new ArrayList<>());
            for (String depId : deps) {
                topologicalSort(depId, visited, inProgress, sortedIds);
            }
            inProgress.remove(taskId);
            visited.add(taskId);
            sortedIds.add(taskId);
        }
    }

    // Get total execution time of all tasks
    public int getTotalExecutionTime() {
        return tasks.values().stream().mapToInt(Task::getExecutionTime).sum();
    }

    // Clear all tasks and dependencies
    public void clear() {
        tasks.clear();
        dependencies.clear();
    }
}