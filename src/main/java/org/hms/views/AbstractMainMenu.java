package org.hms.views;

public abstract class AbstractMainMenu extends AbstractMenu {


    public void logout() {
        app.setCurrentMenu(new AuthenticationMenu(app));
    }
}
