package com.quant.quantara;

import com.quant.quantara.external.config.Configuration;
import com.quant.quantara.external.config.UserConfiguration;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class QuantaraBootstrap extends Application {

    @Override
    public void start(Stage stage) {
        // 리소스 디렉토리 존재 유무 체크
        checkResourcesDir();
        UserConfiguration config = Configuration.getUserConfiguration();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(QuantaraBootstrap.class.getResource("main-view.fxml"));
            fxmlLoader.setClassLoader(QuantaraBootstrap.class.getClassLoader());

            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, config.getUiWidth(), config.getUiHeight());
            stage.setScene(scene);
            stage.setTitle("Quantara");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("FXML 로드 중 예외가 발생했습니다!", e);
        }
    }

    private void checkResourcesDir() {
        Path dir = Paths.get("priv-resources");
        if (Files.notExists(dir))
            throw new IllegalArgumentException("\"priv-resources\" 디렉토리를 찾을 수 없습니다!");
    }

    public static void main(String[] args) {
        launch();
    }

}