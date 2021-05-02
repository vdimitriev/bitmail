package mk.vedmak.bitmail;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import mk.vedmak.bitmail.view.ViewFactory;

public class Launcher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        final ViewFactory viewFactory = new ViewFactory(new EmailManager());
        //viewFactory.showOptionsWindow();
        viewFactory.showLoginWindow();
        //viewFactory.showMainWindow();
    }
}
