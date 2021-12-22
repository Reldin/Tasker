package sample.controllers;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import sample.View;

import java.io.IOException;

public class RootLayoutController {

    private View view;
    private IntegerProperty fontSize = new SimpleIntegerProperty(20);
    @FXML
    private ToolBar RootToolBar;
    @FXML
    private BorderPane RootBorderPane;
    @FXML
    private ImageView RootShutDownImageView;


    public RootLayoutController(View view) {
        this.view = view;
        view.test();
    }

    @FXML
    protected void initialize() {
        /*BackgroundSize bSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
        Background background = new Background(new BackgroundImage(new Image("disco.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bSize));
        RootBorderPane.setBackground(background);*/
        fontSize.bind(RootBorderPane.widthProperty().add(RootBorderPane.heightProperty()).divide(120));
        RootToolBar.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";"));
        RootToolBar.prefHeightProperty().bind(RootBorderPane.heightProperty().divide(14));
        Thread printingHook = new Thread(() -> System.out.println("In the middle of a shutdown"));
        Runtime.getRuntime().addShutdownHook(printingHook);
    }

    /**
     * Changes to the taskView that is generated with code.
     * Basically puts the created GridPane and its children to the center of the parent
     * BorderPane
     * @param event event
     */
    @FXML
    void AddTaskView(ActionEvent event) throws IOException {
       view.setTaskerToCenter();
    }


    /**
     * Shuts down the program
     * @param event When clicked the program shuts down
     */
    @FXML
    void shutDown(MouseEvent event) {
        Platform.exit();
    }

}
