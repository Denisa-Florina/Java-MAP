package src.lab7;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import src.lab7.service.Service;
import src.lab7.service.ServiceLogin;
import src.lab7.service.ServiceController;


public class HelloUser extends Application{
    private ServiceLogin serviceLogin;
    private Service service;
    private ServiceController serviceController;

    public void set(ServiceLogin serviceLogin, Service service, ServiceController serviceController) {
        this.serviceLogin = serviceLogin;
        this.service = service;
        this.serviceController = serviceController;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLController fxmlController = new FXMLController();
            fxmlController.setServiceLogin(serviceLogin, service, serviceController);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            loader.setController(fxmlController);

            TabPane root = loader.load();

            Scene scene = new Scene(root, 600, 400);
            primaryStage.setTitle("Welcome");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(Stage args) {
        launch(String.valueOf(args));
    }
}
