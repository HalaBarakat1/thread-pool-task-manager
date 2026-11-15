package singleton;

import task.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum TasksList {

    //Singleton
    INSTANCE;

    private List<Task> tasks;

    TasksList() {
        // Several pool threads may add tasks at the same time,
        // so we use a thread-safe (synchronized) list instead of a plain ArrayList.
        tasks = Collections.synchronizedList(new ArrayList<>());
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public String list() {
        StringBuilder stringBuilder = new StringBuilder();
        // Iterating a synchronized list must be done while holding its lock,
        // otherwise another thread could add a task in the middle of the loop.
        synchronized (tasks) {
            tasks.forEach(task -> stringBuilder.append(task));
        }
        return stringBuilder.toString();
    }
}
