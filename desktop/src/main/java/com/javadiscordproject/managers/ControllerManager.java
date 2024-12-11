// ControllerManager.java
package com.javadiscordproject.managers;

import java.util.HashMap;
import java.util.Map;

public class ControllerManager {
    private static ControllerManager instance;
    private final Map<String, Object> controllers = new HashMap<>();

    private ControllerManager() {}

    public static ControllerManager getInstance() {
        if (instance == null) {
            instance = new ControllerManager();
        }
        return instance;
    }

    public void registerController(String name, Object controller) {
        controllers.put(name, controller);
    }

    public Object getController(String name) {
        return controllers.get(name);
    }
}