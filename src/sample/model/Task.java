package sample.model;

public interface Task {

    double getId();
    void setId(double id);
    String getTask();
    void setTask(String task);
    public String getTitle();
    public void setTitle(String title);
    boolean getDone();
    void setDone(boolean done);
}
