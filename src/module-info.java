module bitmail {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.web;
    requires activation;
    requires java.mail;
    requires java.desktop;

    opens mk.vedmak.bitmail;
    opens mk.vedmak.bitmail.view;
    opens mk.vedmak.bitmail.controller;
    opens mk.vedmak.bitmail.model;
}