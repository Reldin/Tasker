package sample.model;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface TaskManagement {
    void addTask(Task task) throws IOException;
    void removeTask(Task task);
    List<Task> getTasks();
    Task getTaskByTitle(String title);
    void editTask(Task givenTask, String editedTask, String editedTitle);

}
