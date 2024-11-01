package org.hms.views;

import org.hms.App;

//TODO: Discuss - should Menus be singletons?
public abstract class AbstractMenu {
    protected App app;

    public abstract void displayAndExecute();
}
