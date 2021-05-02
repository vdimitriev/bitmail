package mk.vedmak.bitmail.controller.services;

import mk.vedmak.bitmail.EmailManager;
import mk.vedmak.bitmail.model.EmailAccount;

public class LoginService {

    EmailAccount emailAccount;
    EmailManager emailManager;

    public LoginService(EmailAccount emailAccount, EmailManager emailManager) {
        this.emailAccount = emailAccount;
        this.emailManager = emailManager;
    }

    public void login() {
        System.out.println("Login method called in login service.");
    }
}
