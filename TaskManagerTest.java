package com.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {
    private TaskManager manager;

    @BeforeEach
    void setUp() {
        manager = new TaskManager();
    }

    @Test
    @DisplayName("Add a single task successfully")
    void testAddSingleTask() {
        Task task = new Task("1", "Test Task", 5);
        manager.addTask(task);
        assertEquals(1, manager.getTotalExecutionTime(), "Total execution time should be 5");
    }

    @Test
    @DisplayName("Add duplicate task throws exception")
    void testAddDuplicateTask() {
        Task task1 = new Task("1", "Test Task", 5);
        Task task2 = new Task("1", "Duplicate Task", 3);
        manager.addTask(task1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.addTask(task2);
        });
        assertEquals("Task with ID 1 already exists", exception.getMessage());
    }

    @Test
    @DisplayName("Add task with null ID throws exception")
    void testAddTaskWithNullId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Task(null, "Test Task", 5);
        });
        assertEquals("Task ID cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Add task with empty name throws exception")
    void testAddTaskWithEmptyName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Task("1", "", 5);
        });
        assertEquals("Task name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Add task with negative execution time throws exception")
    void testAddTaskWithNegativeExecutionTime() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Task("1", "Test Task", -1);
        });
        assertEquals("Execution time cannot be negative", exception.getMessage());
    }

    @Test
    @DisplayName("Add valid dependency")
    void testAddValidDependency() {
        manager.addTask(new Task("1", "Task 1", 5));
        manager.addTask(new Task("2", "Task 2", 3));
        manager.addDependency("2", "1");
        List<Task> schedule = manager.scheduleTasks();
        assertEquals(2, schedule.size());
        assertEquals("1", schedule.get(0).getId(), "Task 1 should be scheduled first");
        assertEquals("2", schedule.get(1).getId(), "Task 2 should be scheduled second");
    }

    @Test
    @DisplayName("Add dependency with non-existent task throws exception")
    void testAddDependencyNonExistentTask() {
        manager.addTask(new Task("1", "Task 1", 5));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.addDependency("2", "1");
        });
        assertEquals("Task 2 does not exist", exception.getMessage());
    }

    @Test
    @DisplayName("Add dependency with non-existent dependency throws exception")
    void testAddDependencyNonExistentDependency() {
        manager.addTask(new Task("1", "Task 1", 5));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.addDependency("1", "2");
        });
        assertEquals("Dependency 2 does not exist", exception.getMessage());
    }

    @Test
    @DisplayName("Add self-dependency throws exception")
    void testAddSelfDependency() {
        manager.addTask(new Task("1", "Task 1", 5));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.addDependency("1", "1");
        });
        assertEquals("Task cannot depend on itself", exception.getMessage());
    }

    @Test
    @DisplayName("Detect cyclic dependency")
    void testCyclicDependency() {
        manager.addTask(new Task("1", "Task 1", 5));
        manager.addTask(new Task("2", "Task 2", 3));
        manager.addTask(new Task("3", "Task 3", 2));
        manager.addDependency("2", "1");
        manager.addDependency("3", "2");
        manager.addDependency("1", "3");
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            manager.scheduleTasks();
        });
        assertTrue(exception.getMessage().contains("Cyclic dependency detected"));
    }

    @Test
    @DisplayName("Schedule tasks with complex dependencies")
    void testComplexDependencyScheduling() {
        manager.addTask(new Task("1", "Task 1", 5));
        manager.addTask(new Task("2", "Task 2", 3));
        manager.addTask(new Task("3", "Task 3", 2));
        manager.addTask(new Task("4", "Task 4", 4));
        manager.addDependency("2", "1");
        manager.addDependency("3", "1");
        manager.addDependency("4", "2");
        manager.addDependency("4", "3");
        List<Task> schedule = manager.scheduleTasks();
        assertEquals(4, schedule.size());
        assertEquals("1", schedule.get(0).getId(), "Task 1 should be first");
        assertTrue(schedule.indexOf(manager.getTask("1")) < schedule.indexOf(manager.getTask("2")));
        assertTrue(schedule.indexOf(manager.getTask("1")) < schedule.indexOf(manager.getTask("3")));
        assertTrue(schedule.indexOf(manager.getTask("2")) < schedule.indexOf(manager.getTask("4")));
        assertTrue(schedule.indexOf(manager.getTask("3")) < schedule.indexOf(manager.getTask("4")));
    }

    @Test
    @DisplayName("Schedule tasks with no dependencies")
    void testNoDependenciesScheduling() {
        manager.addTask(new Task("1", "Task 1", 5));
        manager.addTask(new Task("2", "Task 2", 3));
        List<Task> schedule = manager.scheduleTasks();
        assertEquals(2, schedule.size());
        assertTrue(schedule.contains(manager.getTask("1")));
        assertTrue(schedule.contains(manager.getTask("2")));
    }

    @Test
    @DisplayName("Empty task list returns empty schedule")
    void testEmptyTaskList() {
        List<Task> schedule = manager.scheduleTasks();
        assertTrue(schedule.isEmpty(), "Schedule should be empty for no tasks");
    }

    @Test
    @DisplayName("Clear tasks and dependencies")
    void testClearTasks() {
        manager.addTask(new Task("1", "Task 1", 5));
        manager.addTask(new Task("2", "Task 2", 3));
        manager.addDependency("2", "1");
        manager.clear();
        assertEquals(0, manager.getTotalExecutionTime(), "Total execution time should be 0 after clear");
        assertTrue(manager.scheduleTasks().isEmpty(), "Schedule should be empty after clear");
    }

    @Test
    @DisplayName("Total execution time calculation")
    void testTotalExecutionTime() {
        manager.addTask(new Task("1", "Task 1", 5));
        manager.addTask(new Task("2", "Task 2", 3));
        manager.addTask(new Task("3", "Task 3", 2));
        assertEquals(10, manager.getTotalExecutionTime(), "Total execution time should be 10");
    }

    @Test
    @DisplayName("Schedule large number of tasks")
    void testLargeTaskSet() {
        for (int i = 1; i <= 100; i++) {
            manager.addTask(new Task(String.valueOf(i), "Task " + i, i % 10));
            if (i > 1) {
                manager.addDependency(String.valueOf(i), String.valueOf(i - 1));
            }
        }
        List<Task> schedule = manager.scheduleTasks();
        assertEquals(100, schedule.size());
        for (int i = 1; i < 100; i++) {
            assertTrue(schedule.indexOf(manager.getTask(String.valueOf(i))) < 
                      schedule.indexOf(manager.getTask(String.valueOf(i + 1))));
        }
    }

    @Test
    @DisplayName("Handle disconnected task groups")
    void testDisconnectedTaskGroups() {
        manager.addTask(new Task("1", "Task 1", 5));
        manager.addTask(new Task("2", "Task 2", 3));
        manager.addTask(new Task("3", "Task 3", 2));
        manager.addTask(new Task("4", "Task 4", 4));
        manager.addDependency("2", "1");
        manager.addDependency("4", "3");
        List<Task> schedule = manager.scheduleTasks();
        assertEquals(4, schedule.size());
        assertTrue(schedule.indexOf(manager.getTask("1")) < schedule.indexOf(manager.getTask("2")));
        assertTrue(schedule.indexOf(manager.getTask("3")) < schedule.indexOf(manager.getTask("4")));
    }

    @Test
    @DisplayName("Add multiple dependencies to single task")
    void testMultipleDependencies() {
        manager.addTask(new Task("1", "Task 1", 5));
        manager.addTask(new Task("2", "Task 2", 3));
        manager.addTask(new Task("3", "Task 3", 2));
        manager.addTask(new Task("4", "Task 4", 4));
        manager.addDependency("4", "1");
        manager.addDependency("4", "2");
        manager.addDependency("4", "3");
        List<Task> schedule = manager.scheduleTasks();
        assertEquals(4, schedule.size());
        assertTrue(schedule.indexOf(manager.getTask("1")) < schedule.indexOf(manager.getTask("4")));
        assertTrue(schedule.indexOf(manager.getTask("2")) < schedule.indexOf(manager.getTask("4")));
        assertTrue(schedule.indexOf(manager.getTask("3")) < schedule.indexOf(manager.getTask("4")));
    }

    @Test
    @DisplayName("Task equality based on ID")
    void testTaskEquality() {
        Task task1 = new Task("1", "Task 1", 5);
        Task task2 = new Task("1", "Different Name", 10);
        assertEquals(task1, task2, "Tasks with same ID should be equal");
    }

    // Helper method for tests to get task by ID
    private Task getTask(String id) {
        return manager.scheduleTasks().stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}