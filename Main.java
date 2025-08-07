package com.taskmanager;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        // Create sample tasks
        manager.addTask(new Task("1", "Compile Code", 5));
        manager.addTask(new Task("2", "Run Tests", 3));
        manager.addTask(new Task("3", "Package Artifact", 2));
        manager.addTask(new Task("4", "Deploy", 4));

        // Add dependencies
        manager.addDependency("2", "1"); // Tests depend on compilation
        manager.addDependency("3", "2"); // Packaging depends on tests
        manager.addDependency("4", "3"); // Deployment depends on packaging

        // Schedule and print tasks
        try {
            List<Task> schedule = manager.scheduleTasks();
            System.out.println("Task execution order:");
            for (Task task : schedule) {
                System.out.println(task);
            }
            System.out.println("Total execution time: " + manager.getTotalExecutionTime() + " units");
        } catch (IllegalStateException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}