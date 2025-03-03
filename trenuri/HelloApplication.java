package src.trenuri;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import src.trenuri.repository.CityRepo;
import src.trenuri.repository.TrainStationRepo;
import src.trenuri.service.Service;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        CityRepo cityRepository = new CityRepo("jdbc:postgresql://localhost:5432/trenuri", "postgres", "denisa");
        TrainStationRepo trainStationRepo = new TrainStationRepo("jdbc:postgresql://localhost:5432/trenuri", "postgres", "denisa");
        Service service = new Service(cityRepository, trainStationRepo);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        HelloController controller = fxmlLoader.getController();
        controller.setService(service);

        stage.setTitle("Welcome");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}