package com.taskmanager;

// Represents a task with an ID, name, and execution time
public class Task {
    private final String id;
    private final String name;
    private final int executionTime;

    // Constructor with validation
    public Task(String id, String name, int executionTime) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Task ID cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Task name cannot be null or empty");
        }
        if (executionTime < 0) {
            throw new IllegalArgumentException("Execution time cannot be negative");
        }
        this.id = id;
        this.name = name;
        this.executionTime = executionTime;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    @Override
    public String toString() {
        return "Task{id='" + id + "', name='" + name + "', executionTime=" + executionTime + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id.equals(task.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}