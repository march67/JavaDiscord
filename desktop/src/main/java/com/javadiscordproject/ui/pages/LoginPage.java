package com.javadiscordproject.ui.pages;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.Parent;

/**
 * The LoginPage class represents the login page layout of the application.
 * It initializes the basic UI components, including a title, and provides the root element for display.
 */
public class LoginPage {

    private final VBox root;

    /**
     * Constructor for LoginPage.
     * Initializes the root layout and adds the title element.
     */
    public LoginPage() {
        root = new VBox();
        root.setSpacing(20);

        Text title = new Text("Login");
        root.getChildren().add(title);
    }

    /**
     * Returns the root container of the login page.
     * This root element is used as the base component for rendering this page.
     *
     * @return Parent element containing the login page UI.
     */
    public Parent getRoot() {
        return root;
    }
}
