module Tasker {
    requires javafx.fxml;
    requires javafx.controls;
    requires org.junit.jupiter.api;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens sample;
    opens sample.controllers to javafx.fxml;
}