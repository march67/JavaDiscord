package com.javadiscordproject.controllers.base;

import com.javadiscordproject.managers.SceneManager;

abstract public class BaseViewController {
    protected SceneManager sceneManager;

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }
}