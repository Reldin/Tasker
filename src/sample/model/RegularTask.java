package sample.model;

import java.io.Serializable;

public class RegularTask implements Task, Serializable {
    private double id;
    private String task;
    private String title;
    private boolean done = false;
    public static final long serialVersionUID = 1L;


    public RegularTask(String title, String task) {
        this.title = title;
        this.task = task;

    }

    public RegularTask(double id, String task, String title) {
        this.id = id;
        this.title = title;
        this.task = task;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean getDone() {
        return this.done;
    }

    @Override
    public void setDone(boolean done) {
        this.done = done;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean equals(Object o) {
        if(!(o instanceof Task)) {
            return false;
        }
        Task other = (RegularTask)o;
        return id == other.getId() && task.equals(other.getTask());
    }

    public int hashCode() {
        return task.hashCode();
    }
}
