package sample.model;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskManagementManager implements TaskManagement {

    private List<Task> taskList;
    private TaskDAO taskDAO;


    public TaskManagementManager() {
        this.taskList = new ArrayList<>();
        this.taskDAO = new TaskDAO();

    }

    @Override
    public void addTask(Task task) throws IOException {
        taskDAO.addTask(task);
    }

    @Override
    public void removeTask(Task task) {
        taskDAO.removeTask(task);
    }

    @Override
    public List<Task> getTasks() {
        return taskDAO.getTasks();
    }

    @Override
    public Task getTaskByTitle(String title) {
        Optional<Task> task = taskDAO.getTaskByTitle(title);
        return task.orElse(null);
    }

    @Override
    public void editTask(Task givenTask, String editedTitle, String editedTask) {
       taskDAO.editTask(givenTask, editedTitle, editedTask);
    }
}
