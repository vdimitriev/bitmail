package mk.vedmak.bitmail.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mk.vedmak.bitmail.EmailManager;
import mk.vedmak.bitmail.controller.*;

import java.util.ArrayList;

public class ViewFactory {

    private EmailManager emailManager;
    private ArrayList<Stage> activeStages;
    private boolean mainViewInitialized = false;

    public ViewFactory(EmailManager emailManager) {
        this.emailManager = emailManager;
        activeStages = new ArrayList<>();
    }

    private ColorTheme colorTheme = ColorTheme.DEFAULT;
    private FontSize fontSize = FontSize.MEDIUM;

    public void showLoginWindow() {
        final BaseController controller = new LoginWindowController(emailManager, this, "LoginWindow.fxml");
        initStage(controller);
    }

    public void showMainWindow() {
        final BaseController controller = new MainWindowController(emailManager, this, "MainWindow.fxml");
        initStage(controller);
        mainViewInitialized = true;
    }

    public void showOptionsWindow() {
        System.out.println("options window called");
        final BaseController controller = new OptionsWindowController(emailManager, this, "OptionsWindow.fxml");
        initStage(controller);
    }

    public void showComposeMessageWindow() {
        System.out.println("compose message window called");
        final BaseController controller = new ComposeMessageController(emailManager, this, "ComposeMessageWindow.fxml");
        initStage(controller);
    }

    public void closeStage(Stage stageToClose) {
        stageToClose.close();
        activeStages.remove(stageToClose);
    }

    private void initStage(BaseController controller) {
        final FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(controller.getFxmlName()));
        fxmlLoader.setController(controller);
        Parent parent;
        try {
            parent = fxmlLoader.load();
        } catch (Exception e) {
            System.out.println("Exception " + e);
            return;
        }

        final Scene scene = new Scene(parent);
        final Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

        activeStages.add(stage);
    }

    public ColorTheme getColorTheme() {
        return colorTheme;
    }

    public FontSize getFontSize() {
        return fontSize;
    }

    public void setColorTheme(ColorTheme colorTheme) {
        this.colorTheme = colorTheme;
    }

    public void setFontSize(FontSize fontSize) {
        this.fontSize = fontSize;
    }

    public void updateStyles() {
        for(Stage stage: activeStages) {
            Scene scene = stage.getScene();
            scene.getStylesheets().clear();
            String cssPath = ColorTheme.getCssPath(colorTheme);
            String form = getClass().getResource(cssPath).toExternalForm();
            scene.getStylesheets().add(form);

            scene.getStylesheets().add(getClass().getResource(FontSize.getCssPath(fontSize)).toExternalForm());
        }
    }

    public boolean isMainViewInitialized() {
        return mainViewInitialized;
    }
}
