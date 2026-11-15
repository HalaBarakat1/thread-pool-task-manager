package singleton;

import task.*;

import java.text.DecimalFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TaskExecutor {

    //Singleton
    INSTANCE;

    public static final String TASKS_PACKAGE = "task.";

    public String run(String parameters) {
        String taskName, input = "";

        List<String> list = Arrays.stream(parameters.split("&")).collect(Collectors.toList());

        long start = System.currentTimeMillis();
        if (list.size() != 2) {
            taskName = "TaskIncorrectParams";
        } else {
            taskName = list.get(0);
            input = list.get(1);
        }
        Task task = createTask(taskName);
        task.setInput(input);

        log("START " + taskName + " (" + input + ")");
        task.execute();

        TasksList.INSTANCE.add(task);
        long end = System.currentTimeMillis();
        String executedTime = new DecimalFormat("#0.00").format((end - start) / 1000d) + "s";
        task.setExecutedTime(executedTime);
        log("END   " + taskName + " (" + input + ") in " + executedTime);

        return task.getResult();
    }

    /**
     * Prints a log line with the current time and the name of the thread
     * that is running the task, e.g.:
     * [12:30:05.123] [pool-1-thread-2] START TaskPrimeCount (1,5000000)
     */
    private void log(String message) {
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
        String thread = Thread.currentThread().getName();
        System.out.println("[" + time + "] [" + thread + "] " + message);
    }

    private Task createTask(String className) {
        Task task = null;
        try {
            task = (Task)Class.forName(TASKS_PACKAGE + className).newInstance();
        } catch (Exception e) {
            try {
                task = (Task)Class.forName(TASKS_PACKAGE + "TaskNotFound").newInstance();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return task;
    }
}
