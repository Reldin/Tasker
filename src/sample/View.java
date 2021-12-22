package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.controllers.RootLayoutController;
import sample.controllers.TaskerController;

import java.io.IOException;

public class View extends Application {
    private static Stage primaryStage;
    private BorderPane RootBorderPane;
    private static final int height = 1200;
    private static final int width = 1500;


    @Override
    public void init() {
    }

    @Override
    public void start(Stage primaryStageGiven) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/RootLayout.fxml"));


        loader.setControllerFactory(e -> new RootLayoutController(this));
        loader.load();
        RootBorderPane = loader.getRoot();
        primaryStage = primaryStageGiven;
        primaryStage.setTitle("Tasker");
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(600);
        RootBorderPane.minHeight(900);
        RootBorderPane.minWidth(1000);
        Scene scene = new Scene(RootBorderPane, width, height);

        scene.getStylesheets().add("styles.css");
        primaryStage.setScene(scene);
       /* primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);*/
        primaryStage.show();

        setTaskerToCenter();

    }

    public void setTaskView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/RootLayout.fxml"));


        loader.setControllerFactory(e -> new RootLayoutController(this));
        loader.load();
        RootBorderPane = loader.getRoot();
        RootBorderPane.minHeight(900);
        RootBorderPane.minWidth(1000);
        Scene scene = new Scene(RootBorderPane, width, height);

        scene.getStylesheets().add("styles.css");
        primaryStage.setScene(scene);

        loader = new FXMLLoader(getClass().getResource("views/Tasker.fxml"));

        loader.setControllerFactory(e -> new TaskerController(this));
        loader.load();
        Parent root = loader.getRoot();
        RootBorderPane.setCenter(root);
        primaryStage.show();
    }

    public void setTaskerToCenter() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("views/Tasker.fxml"));

        loader.setControllerFactory(e -> new TaskerController(this));
        loader.load();
        Parent root = loader.getRoot();
        RootBorderPane.setCenter(root);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
