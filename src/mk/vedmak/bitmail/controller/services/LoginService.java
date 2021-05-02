package mk.vedmak.bitmail.controller.services;

import mk.vedmak.bitmail.EmailManager;
import mk.vedmak.bitmail.model.EmailAccount;

import javax.mail.*;


public class LoginService {

    EmailAccount emailAccount;
    EmailManager emailManager;

    public LoginService(EmailAccount emailAccount, EmailManager emailManager) {
        this.emailAccount = emailAccount;
        this.emailManager = emailManager;
    }

    public EmailLoginResult login() {
        System.out.println("Login method called in login service.");
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount.getAddress(), emailAccount.getPassword());
            }
        };

        try {
            Session session = Session.getInstance(emailAccount.getProperties(), authenticator);
            Store store = session.getStore("imaps");
            store.connect(emailAccount.getProperties().getProperty("incomingHost"), emailAccount.getAddress(), emailAccount.getPassword());
            emailAccount.setStore(store);
            return EmailLoginResult.SUCCESS;
        } catch(AuthenticationFailedException e) {
            e.printStackTrace();
            return EmailLoginResult.FAILED_BY_CREDENTIALS;
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            return EmailLoginResult.FAILED_BY_NETWORK;
        } catch (MessagingException e) {
            e.printStackTrace();
            return EmailLoginResult.FAILED_BY_UNEXPECTED_ERROR;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            return EmailLoginResult.FAILED_BY_UNEXPECTED_ERROR;
        }
    }
}
