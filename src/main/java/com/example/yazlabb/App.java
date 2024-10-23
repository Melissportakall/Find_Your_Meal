package com.example.yazlabb;

import Controller.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("deneme.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Yemekteyiz");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // Veri tabanı bağlantısını test edelim
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Bağlantı başarılı!");
                DatabaseConnection.getTarifler();
            } else {
                System.out.println("Bağlantı başarısız.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        launch();
    }
}