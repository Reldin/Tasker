package sample.controllers;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import sample.View;
import sample.model.*;

import java.util.Random;

/**
 * @author Jani Peltonen
 * @version 0.0.1
 */
public class TaskerController {

    @FXML
    private BorderPane BorderPane;

    @FXML
    private ListView<Task> BorderListView;


    @FXML
    private Button AddTaskButton;


    @FXML
    private Button AddToTaskList;


    @FXML
    private TextArea TaskTextArea;

    @FXML
    private GridPane MainGrid;

    @FXML
    private TextField TaskTextField;

    @FXML
    private VBox TaskVbox;

    @FXML
    private TabPane TaskTabPane;

    @FXML
    private ScrollPane TaskScrollPane;

    TaskDAO taskDAO;
    private String lightBlackColor = "#2A363B";
    private IntegerProperty fontSize = new SimpleIntegerProperty(20);

    private TaskManagement taskManagement;


    private ObservableList<Task> taskObservableList;

    private double scrollSpeed = 0.1;


    private View view;


    public TaskerController(){
        taskManagement = new TaskManagementManager();
    }
    public TaskerController(View view) {
        this.view = view;
        taskManagement = new TaskManagementManager();
        taskDAO = new TaskDAO();
    }
    @FXML
    protected void initialize() {


        fontSize.bind(BorderPane.widthProperty().add(BorderPane.heightProperty()).divide(120));
        BorderPane.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";"));
        TaskTabPane.prefWidthProperty().bind(BorderPane.widthProperty().add(BorderPane.heightProperty()).divide(6).add(1));
        TaskScrollPane.prefHeightProperty().bind(BorderPane.heightProperty().divide(3).add(1));
        TaskScrollPane.getContent().setOnScroll(scrollEvent ->  {
            double deltaY = scrollEvent.getDeltaY() * scrollSpeed;
            TaskScrollPane.setVvalue(TaskScrollPane.getVvalue() - deltaY);
        });
        Platform.runLater(() -> {
        taskObservableList = FXCollections.observableArrayList(taskManagement.getTasks());
            BorderListView.setCellFactory(param -> new ListCell<Task>() {
                @Override
                protected void updateItem(Task item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty || item == null || item.getTitle() == null) {
                        setText(null);
                    } else {
                        setText(item.getTitle());
                    }
                }
            });
            BorderListView.setOnMouseClicked(event -> {
                Platform.runLater(this::showTaskDetails);
            });

            TaskVbox.setOnMouseClicked(event -> {
                if(!TaskVbox.getChildren().isEmpty() && event.isStillSincePress()){
                    if(BorderPane.getBottom() == null) {
                        nodeLeaveManipulationTask();
                    } else {
                        nodeManipulationTask();
                    }
                }

            });

            BorderListView.setItems(taskObservableList);
            TaskTextArea.setOnKeyPressed(keyEvent -> {
                if(keyEvent.getCode() == KeyCode.ENTER && keyEvent.isShiftDown()) {
                    addToTaskListEvent();
                }
            });
        });



    }

    /**
     * A helper method that writes a Task interface implementor to a file.
     */
    void addToTaskListEvent() {
        if(!(TaskTextField.getText().isEmpty() && TaskTextField.getText().isBlank())) {
            Random random = new Random();
            double id = random.nextDouble() * 10;
            Task task = new RegularTask(id, TaskTextArea.getText(), TaskTextField.getText());

            taskDAO.addTask(task);
            taskObservableList.add(task);


            BorderListView.refresh();
            TaskTextArea.clear();
            TaskTextField.clear();
        }
    }

    /**
     * Event to add a Task interface implementor to the database
     * @param event event
     */
    @FXML
    void addToTaskList(ActionEvent event) {
        addToTaskListEvent();

    }

    /**
     * BorderPane changes to make the Task details to take all the possible space
     */
    @FXML
    void nodeManipulationTask() {
        BorderPane.getCenter().setManaged(false);
        BorderPane.getLeft().setManaged(false);
        BorderPane.getChildren().remove(MainGrid);
        BorderPane.getChildren().remove(TaskScrollPane);
        BorderPane.getChildren().remove(TaskTabPane);
        BorderPane.setCenter(TaskScrollPane);
    }

    /**
     * BorderPane changes back from the nodeManipulationTask
     */
    @FXML
    void nodeLeaveManipulationTask(){
        BorderListView.refresh();
        BorderPane.setCenter(MainGrid);
        BorderPane.setLeft(TaskTabPane);
        BorderPane.setBottom(TaskScrollPane);
        BorderPane.getCenter().setManaged(true);
        BorderPane.getLeft().setManaged(true);
    }

    /**
     * Event to delete task from the database.
     * Uses the TaskManagementManager class to get access to the TaskDAO.
     * @param selectedItem Task object from the ListView
     */
    @FXML
    void deleteTask(Task selectedItem) {
        taskManagement.removeTask(selectedItem);
        taskObservableList.remove(selectedItem);
        TaskVbox.getChildren().clear();

        if((BorderPane.leftProperty().get() == null))
            nodeLeaveManipulationTask();

    }

    /**
     * Event for editing a task
     * @param selectedItem Task object from the ListView
     * @param editedText Edited String from a TextArea
     * @param editedTitle Edited String from a TextField
     */
    @FXML
    void editTask(Task selectedItem, String editedText, String editedTitle) {
        taskManagement.editTask(selectedItem, editedTitle, editedText);
        taskObservableList.remove(selectedItem);
        taskObservableList.add(new RegularTask(editedTitle, editedText));
        BorderListView.refresh();


    }

    /**
     * Creates a view to a Vbox node that shows details of a task from the ListView
     */
    void showTaskDetails() {
        Task selectedItem = BorderListView.getSelectionModel().getSelectedItem();
        if(selectedItem != null) {
            TaskVbox.getChildren().clear();
            Label label = new Label(selectedItem.getTitle());

            label.setPadding(new Insets(5, 0, 0, 0));
            label.setStyle("-fx-text-fill: black;");

            TextArea VBoxTextArea = new TextArea(selectedItem.getTask());
            VBoxTextArea.setEditable(false);
            VBoxTextArea.maxWidthProperty().bind(TaskVbox.widthProperty().divide(1.5));
            VBoxTextArea.prefHeightProperty().bind(TaskScrollPane.heightProperty().divide(1.2));
            VBoxTextArea.setWrapText(true);
            VBox.setMargin(VBoxTextArea, new Insets(0, 0, 10, 0));
            VBoxTextArea.setStyle("-fx-text-fill: black;");
            TextField editTextFieldTitle = new TextField();
            TaskVbox.setFillWidth(false);
            Button deleteButton = new Button("Delete Task");
            deleteButton.setEffect(new DropShadow());
            deleteButton.setOnAction(event1 -> {
                deleteTask(selectedItem);
            });
            Button editButton = new Button("Edit Task");
            editButton.setEffect(new DropShadow());
            editButton.setOnAction(editEvent -> {
                if(editButton.getText().equals("Save Task")) {
                    editTask(selectedItem, VBoxTextArea.getText(), editTextFieldTitle.getText());
                    editButton.setText("Edit Task");
                    VBoxTextArea.setEditable(false);
                    label.setText(editTextFieldTitle.getText());
                    TaskVbox.getChildren().remove(0);
                    TaskVbox.getChildren().add(0, label);
                    BorderListView.refresh();
                } else {
                    editButton.setText("Save Task");
                    editTextFieldTitle.setText(selectedItem.getTitle());
                    VBoxTextArea.setEditable(true);
                    TaskVbox.getChildren().remove(0);
                    TaskVbox.getChildren().add(0, editTextFieldTitle);

                }
            });
            HBox hbox = new HBox();
            HBox.setMargin(deleteButton, new Insets(0, 10, 0, 0));
            VBox.setMargin(hbox, new Insets(10, 0, 0, 0));
            hbox.setAlignment(Pos.CENTER);
            hbox.getChildren().add(deleteButton);
            hbox.getChildren().add(editButton);
            TaskVbox.getChildren().add(label);
            TaskVbox.getChildren().add(VBoxTextArea);
            TaskVbox.getChildren().add(hbox);
            TaskVbox.setSpacing(20);
        }
    }

    private void reportMemoryUsage() {
        System.gc();
        Runtime runtime = Runtime.getRuntime();
        long used = runtime.totalMemory() - runtime.freeMemory();
        double mb = 1024*1024;
        System.out.printf("Used Memory after GC: %.2f MB", used / mb);
        System.out.println();
    }
}
