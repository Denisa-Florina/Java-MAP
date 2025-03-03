package src.trenuri;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import src.trenuri.repository.CityRepo;
import src.trenuri.repository.TrainStationRepo;
import src.trenuri.service.Service;

import java.io.IOException;
import java.util.UUID;

public class HelloController {
    @FXML Button OpenNewWindows;

    private Service service;

    public void setService(Service service) {
        this.service = service;
    }

    @FXML
    private void handleOpenNewWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("trenuriFx.fxml"));
            Parent root = fxmlLoader.load();

            TrenuriController controller = fxmlLoader.getController();
            controller.set((String) UUID.randomUUID().toString(), service);

            Stage stage = new Stage();
            stage.setTitle("Trenuri");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}