package sample.model;

import java.io.Serializable;

public class RegularTask implements Task {
    private String task;
    private String title;
    private boolean done = false;
    public static final long serialVersionUID = 1L;


    public RegularTask(String title, String task) {
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

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

}
