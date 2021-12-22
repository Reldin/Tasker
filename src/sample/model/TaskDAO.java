package sample.model;


import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data access object for the Tasker javafx application.
 * Works with classes that implement the Task interface.
 * @author Jani Peltonen
 */
public class TaskDAO {
    Connection connection = null;

    public TaskDAO () {

        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite::resource:tasker.db");
        } catch (SQLException | ClassNotFoundException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + ": Connection failure " + e.getCause());
            System.exit(0);
        }
        System.out.println("Database opened successfully");
    }

    /**
     * Gets all the tasks from the SQLITE database tasker.db and table Task.
     * @return Returns a list of all the tasks from table Task from a database
     * that implement the Task interface. If nothing is found returns null.
     */
    public List<Task> getTasks() {
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Task")){
            ResultSet rs = preparedStatement.executeQuery();
            List<Task> list = new ArrayList<>();

            while(rs.next()) {
                Task task = new RegularTask(rs.getString("Title"), rs.getString("TaskText"));
                list.add(task);
                System.out.println(rs.getString("Title") + " " + rs.getString("TaskText"));
            }
            rs.close();
            return list;

        } catch (SQLException e) {
            Logger.getAnonymousLogger().log(Level.SEVERE, LocalDateTime.now() + " time " + e.getCause());
        }
        return null;
    }

    /**
     * Adds a task to a SQLITE database tasker.db and table Task
     * @param task Any task that implements the Task interface
     */
    public void addTask(Task task) {

        try(PreparedStatement ps = connection.prepareStatement("INSERT INTO Task VALUES(?, ?, ?);")) {
            ps.setString(1, null);
            ps.setString(2, task.getTitle());
            ps.setString(3, task.getTask());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a task from the SQLITE database tasker.db and table Task
     * @param task Any task that implements the Task interface
     */
    public void removeTask(Task task) {
        try(PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM Task WHERE Title = ? AND TaskText = ?;")) {
            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setString(2, task.getTask());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Edits the title and task text
     * @param givenTask Given task with updated value.
     * @param editedTitle Given String with edited title
     * @param editedTask Given string with edited task text
     */
    public void editTask(Task givenTask, String editedTitle, String editedTask) {
        try(PreparedStatement preparedStatement = connection.prepareStatement("UPDATE Task SET Title = ?, TaskText = ? WHERE Title=? AND TaskText=?;")) {
            preparedStatement.setString(1, editedTitle);
            preparedStatement.setString(2, editedTask);
            preparedStatement.setString(3, givenTask.getTitle());
            preparedStatement.setString(4, givenTask.getTask());
            System.out.println("return int from edit: " + preparedStatement.execute());
        } catch (SQLException e) {
            System.out.println("Error code: " + e.getErrorCode() + " Time: " + LocalDateTime.now());
        }
    }

    /***
     *
     * @param givenTitle User give String to search by title.
     * @return RegularTask if nothing found return null
     */
    public Optional<Task> getTaskByTitle(String givenTitle) {
        try(PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Task WHERE Title=?")) {

            preparedStatement.setString(1, givenTitle);
            ResultSet resultSet = preparedStatement.executeQuery();
            Task editedTask = new RegularTask(resultSet.getString("Title"), resultSet.getString("TaskText"));

            resultSet.close();

            return Optional.of(editedTask);
        } catch (Exception e) {
            System.out.println("getTask error code = " + e.getMessage());
        }
        return Optional.empty();
    }
}
