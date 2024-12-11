package com.javadiscordproject.controllers.component;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import java.net.URL;

public class LeftNavbarController {
    @FXML
    private VBox navbarContainer;

    @FXML
    public void initialize() {
        try {
            URL cssUrl = getClass().getResource("/styles/component/auth-component.css");
            if (cssUrl != null) {
                String cssPath = cssUrl.toExternalForm();
                navbarContainer.getStylesheets().add(cssPath);
            } else {
                System.err.println("CSS file not found");
            }
        } catch (NullPointerException e) {
            System.err.println("Failed to load CSS: " + e.getMessage());
        }
    }
}