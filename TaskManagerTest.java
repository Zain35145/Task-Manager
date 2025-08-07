package com.taskmanager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskManagerTest {
    private TaskManager manager;

    @BeforeEach
    void setUp() {
        // Initialize a fresh TaskManager for each test to ensure isolation
        manager = new TaskManager();
    }

    // Task Class Tests
    @Test
    @DisplayName("Create task with valid inputs")
    void testCreateValidTask() {
        Task task = new Task("T1", "Build Module", 10);
        assertEquals("T1", task.getId(), "Task ID should match input");
        assertEquals("Build Module", task.getName(), "Task name should match input");
        assertEquals(10, task.getExecutionTime(), "Execution time should match input");
    }

    @Test
    @DisplayName("Task creation with null ID throws exception")
    void testTaskNullId() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Task(null, "Build Module", 10);
        });
        assertEquals("Task ID cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Task creation with empty name throws exception")
    void testTaskEmptyName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Task("T1", "", 10);
        });
        assertEquals("Task name cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Task creation with negative execution time throws exception")
    void testTaskNegativeExecutionTime() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Task("T1", "Build Module", -5);
        });
        assertEquals("Execution time cannot be negative", exception.getMessage());
    }

    @Test
    @DisplayName("Task equality based on ID")
    void testTaskEqualityById() {
        Task task1 = new Task("T1", "Build Module", 10);
        Task task2 = new Task("T1", "Different Name", 5);
        assertEquals(task1, task2, "Tasks with same ID should be equal");
    }

    @Test
    @DisplayName("Task toString includes all fields")
    void testTaskToString() {
        Task task = new Task("T1", "Build Module", 10);
        assertTrue(task.toString().contains("T1") && task.toString().contains("Build Module") && task.toString().contains("10"));
    }

    // TaskManager: Task Addition Tests
    @Test
    @DisplayName("Add single task successfully")
    void testAddSingleTask() {
        manager.addTask(new Task("T1", "Build Module", 10));
        assertEquals(10, manager.getTotalExecutionTime(), "Total execution time should be 10");
    }

    @Test
    @DisplayName("Add duplicate task throws exception")
    void testAddDuplicateTask() {
        manager.addTask(new Task("T1", "Build Module", 10));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.addTask(new Task("T1", "Test Module", 5));
        });
        assertEquals("Task with ID T1 already exists", exception.getMessage());
    }

    @Test
    @DisplayName("Add task with long ID")
    void testAddTaskLongId() {
        String longId = "T".repeat(100);
        manager.addTask(new Task(longId, "Long ID Task", 5));
        assertEquals(5, manager.getTotalExecutionTime(), "Task with long ID should be added");
    }

    @Test
    @DisplayName("Add multiple tasks")
    void testAddMultipleTasks() {
        manager.addTask(new Task("T1", "Task 1", 5));
        manager.addTask(new Task("T2", "Task 2", 3));
        manager.addTask(new Task("T3", "Task 3", 2));
        assertEquals(10, manager.getTotalExecutionTime(), "Total execution time should sum to 10");
    }

    // TaskManager: Dependency Management Tests
    @Test
    @DisplayName("Add valid dependency")
    void testAddValidDependency() {
        manager.addTask(new Task("T1", "Task 1", 5));
        manager.addTask(new Task("T2", "Task 2", 3));
        manager.addDependency("T2", "T1");
        List<Task> schedule = manager.scheduleTasks();
        assertEquals(2, schedule.size());
        assertEquals("T1", schedule.get(0).getId(), "Task 1 should be scheduled first");
        assertEquals("T2", schedule.get(1).getId(), "Task 2 should be scheduled second");
    }

    @Test
    @DisplayName("Add dependency with non-existent task throws exception")
    void testAddDependencyNonExistentTask() {
        manager.addTask(new Task("T1", "Task 1", 5));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.addDependency("T2", "T1");
        });
        assertEquals("Task T2 does not exist", exception.getMessage());
    }

    @Test
    @DisplayName("Add self-dependency throws exception")
    void testAddSelfDependency() {
        manager.addTask(new Task("T1", "Task 1", 5));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.addDependency("T1", "T1");
        });
        assertEquals("Task cannot depend on itself", exception.getMessage());
    }

    @Test
    @DisplayName("Add multiple dependencies to one task")
    void testAddMultipleDependencies() {
        manager.addTask(new Task("T1", "Task 1", 5));
        manager.addTask(new Task("T2", "Task 2", 3));
        manager.addTask(new Task("T3", "Task 3", 2));
        manager.addDependency("T3", "T1");
        manager.addDependency("T3", "T2");
        List<Task> schedule = manager.scheduleTasks();
        assertTrue(schedule.indexOf(getTask("T1")) < schedule.indexOf(getTask("T3")));
        assertTrue(schedule.indexOf(getTask("T2")) < schedule.indexOf(getTask("T3")));
    }

    @Test
    @DisplayName("Add dependency with long task IDs")
    void testAddDependencyLongIds() {
        String longId1 = "T".repeat(50);
        String longId2 = "U".repeat(50);
        manager.addTask(new Task(longId1, "Task 1", 5));
        manager.addTask(new Task(longId2, "Task 2", 3));
        manager.addDependency(longId2, longId1);
        List<Task> schedule = manager.scheduleTasks();
        assertEquals(longId1, schedule.get(0).getId());
        assertEquals(longId2, schedule.get(1).getId());
    }

    // TaskManager: Scheduling Tests
    @Test
    @DisplayName("Schedule tasks with linear dependencies")
    void testLinearDependencyScheduling() {
        manager.addTask(new Task("T1", "Task 1", 5));
        manager.addTask(new Task("T2", "Task 2", 3));
        manager.addTask(new Task("T3", "Task 3", 2));
        manager.addDependency("T2", "T1");
        manager.addDependency("T3", "T2");
        List<Task> schedule = manager.scheduleTasks();
        assertEquals(3, schedule.size());
        assertEquals("T1", schedule.get(0).getId());
        assertEquals("T2", schedule.get(1).getId());
        assertEquals("T3", schedule.get(2).getId());
    }

    @Test
    @DisplayName("Detect cyclic dependency")
    void testCyclicDependency() {
        manager.addTask(new Task("T1", "Task 1", 5));
        manager.addTask(new Task("T2", "Task 2", 3));
        manager.addTask(new Task("T3", "Task 3", 2));
        manager.addDependency("T2", "T1");
        manager.addDependency("T3", "T2");
        manager.addDependency("T1", "T3");
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            manager.scheduleTasks();
        });
        assertTrue(exception.getMessage().contains("Cyclic dependency detected"));
    }

    @Test
    @DisplayName("Schedule disconnected task groups")
    void testDisconnectedTaskGroups() {
        manager.addTask(new Task("T1", "Task 1", 5));
        manager.addTask(new Task("T2", "Task 2", 3));
        manager.addTask(new Task("T3", "Task 3", 2));
        manager.addTask(new Task("T4", "Task 4", 4));
        manager.addDependency("T2", "T1");
        manager.addDependency("T4", "T3");
        List<Task> schedule = manager.scheduleTasks();
        assertEquals(4, schedule.size());
        assertTrue(schedule.indexOf(getTask("T1")) < schedule.indexOf(getTask("T2")));
        assertTrue(schedule.indexOf(getTask("T3")) < schedule.indexOf(getTask("T4")));
    }

    @Test
    @DisplayName("Schedule empty task list")
    void testEmptySchedule() {
        List<Task> schedule = manager.scheduleTasks();
        assertTrue(schedule.isEmpty(), "Empty task list should return empty schedule");
    }

    @Test
    @DisplayName("Schedule single task without dependencies")
    void testSingleTaskSchedule() {
        manager.addTask(new Task("T1", "Task 1", 5));
        List<Task> schedule = manager.scheduleTasks();
        assertEquals(1, schedule.size());
        assertEquals("T1", schedule.get(0).getId());
    }

    @Test
    @DisplayName("Schedule large task set")
    void testLargeTaskSet() {
        for (int i = 1; i <= 50; i++) {
            manager.addTask(new Task("T" + i, "Task " + i, i % 10));
            if (i > 1) {
                manager.addDependency("T" + i, "T" + (i - 1));
            }
        }
        List<Task> schedule = manager.scheduleTasks();
        assertEquals(50, schedule.size());
        for (int i = 1; i < 50; i++) {
            assertTrue(schedule.indexOf(getTask("T" + i)) < schedule.indexOf(getTask("T" + (i + 1))));
        }
    }

    @Test
    @DisplayName("Schedule tasks with complex dependency graph")
    void testComplexDependencyGraph() {
        manager.addTask(new Task("T1", "Task 1", 5));
        manager.addTask(new Task("T2", "Task 2", 3));
        manager.addTask(new Task("T3", "Task 3", 2));
        manager.addTask(new Task("T4", "Task 4", 4));
        manager.addDependency("T2", "T1");
        manager.addDependency("T3", "T1");
        manager.addDependency("T4", "T2");
        manager.addDependency("T4", "T3");
        List<Task> schedule = manager.scheduleTasks();
        assertEquals(4, schedule.size());
        assertTrue(schedule.indexOf(getTask("T1")) < schedule.indexOf(getTask("T2")));
        assertTrue(schedule.indexOf(getTask("T1")) < schedule.indexOf(getTask("T3")));
        assertTrue(schedule.indexOf(getTask("T2")) < schedule.indexOf(getTask("T4")));
        assertTrue(schedule.indexOf(getTask("T3")) < schedule.indexOf(getTask("T4")));
    }

    // TaskManager: Execution Time Tests
    @Test
    @DisplayName("Calculate total execution time for multiple tasks")
    void testTotalExecutionTimeMultipleTasks() {
        manager.addTask(new Task("T1", "Task 1", 5));
        manager.addTask(new Task("T2", "Task 2", 3));
        manager.addTask(new Task("T3", "Task 3", 2));
        assertEquals(10, manager.getTotalExecutionTime(), "Total execution time should be 10");
    }

    @Test
    @DisplayName("Execution time for empty task list")
    void testExecutionTimeEmptyList() {
        assertEquals(0, manager.getTotalExecutionTime(), "Empty task list should have zero execution time");
    }

    @Test
    @DisplayName("Execution time with zero-time tasks")
    void testZeroExecutionTimeTasks() {
        manager.addTask(new Task("T1", "Task 1", 0));
        manager.addTask(new Task("T2", "Task 2", 0));
        assertEquals(0, manager.getTotalExecutionTime(), "Zero execution time tasks should sum to 0");
    }

    // TaskManager: Clear Method Tests
    @Test
    @DisplayName("Clear tasks and dependencies")
    void testClearTasks() {
        manager.addTask(new Task("T1", "Task 1", 5));
        manager.addTask(new Task("T2", "Task 2", 3));
        manager.addDependency("T2", "T1");
        manager.clear();
        assertEquals(0, manager.getTotalExecutionTime(), "Execution time should be 0 after clear");
        assertTrue(manager.scheduleTasks().isEmpty(), "Schedule should be empty after clear");
    }

    @Test
    @DisplayName("Clear empty task manager")
    void testClearEmptyManager() {
        manager.clear();
        assertEquals(0, manager.getTotalExecutionTime(), "Execution time should remain 0");
        assertTrue(manager.scheduleTasks().isEmpty(), "Schedule should remain empty");
    }

    @Test
    @DisplayName("Add tasks after clear")
    void testAddTasksAfterClear() {
        manager.addTask(new Task("T1", "Task 1", 5));
        manager.clear();
        manager.addTask(new Task("T2", "Task 2", 3));
        assertEquals(3, manager.getTotalExecutionTime(), "New task should be added after clear");
    }

    // TaskManager: Boundary Tests
    @Test
    @DisplayName("Add task with maximum integer execution time")
    void testMaxExecutionTime() {
        manager.addTask(new Task("T1", "Task 1", Integer.MAX_VALUE));
        assertEquals(Integer.MAX_VALUE, manager.getTotalExecutionTime(), "Should handle max execution time");
    }

    @Test
    @DisplayName("Schedule tasks with maximum dependencies")
    void testMaxDependencies() {
        manager.addTask(new Task("T1", "Task 1", 5));
        for (int i = 2; i <= 10; i++) {
            manager.addTask(new Task("T" + i, "Task " + i, 3));
            manager.addDependency("T1", "T" + i);
        }
        List<Task> schedule = manager.scheduleTasks();
        assertEquals(10, schedule.size());
        for (int i = 2; i <= 10; i++) {
            assertTrue(schedule.indexOf(getTask("T" + i)) < schedule.indexOf(getTask("T1")));
        }
    }

    // Helper method to retrieve task by ID
    private Task getTask(String id) {
        return manager.scheduleTasks().stream()
                .filter(t -> t.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
