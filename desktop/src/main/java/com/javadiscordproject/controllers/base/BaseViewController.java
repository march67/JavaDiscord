package com.javadiscordproject.controllers.base;

import com.javadiscordproject.services.SceneManager;

abstract public class BaseViewController {
    protected SceneManager sceneManager;

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
}