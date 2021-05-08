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
import mk.vedmak.bitmail.controller.persistence.PersistenceAccess;
import mk.vedmak.bitmail.controller.persistence.ValidAccount;
import mk.vedmak.bitmail.controller.services.LoginService;
import mk.vedmak.bitmail.model.EmailAccount;
import mk.vedmak.bitmail.view.ViewFactory;

import java.util.ArrayList;
import java.util.List;

public class Launcher extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    private PersistenceAccess persistenceAccess = new PersistenceAccess();
    private EmailManager emailManager = new EmailManager();

    @Override
    public void start(Stage stage) throws Exception {
        final ViewFactory viewFactory = new ViewFactory(emailManager);
        checkPersistence(viewFactory);
        //viewFactory.showOptionsWindow();
        //viewFactory.showLoginWindow();
        //viewFactory.showMainWindow();
    }

    private void checkPersistence(ViewFactory viewFactory) {
        List<ValidAccount> validAccountList = persistenceAccess.loadFromPersistence();
        if(validAccountList.size() > 0) {
            viewFactory.showMainWindow();
            for(ValidAccount validAccount: validAccountList) {
                EmailAccount emailAccount = new EmailAccount(validAccount.getAddress(), validAccount.getPassword());
                LoginService loginService = new LoginService(emailAccount, emailManager);
                loginService.start();
            }
        } else {
            viewFactory.showLoginWindow();
        }
    }

    @Override
    public void stop() throws Exception {
        List<ValidAccount> validAccountList = new ArrayList<>();
        for(EmailAccount emailAccount : emailManager.getEmailAccounts()) {
            validAccountList.add(new ValidAccount(emailAccount.getAddress(), emailAccount.getPassword()));
        }
        persistenceAccess.saveToPersistence(validAccountList);
    }
}
