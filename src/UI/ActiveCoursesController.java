package UI;

import LOGIC.GradingSystem;
import javafx.scene.Scene;

public class ActiveCoursesController {

    Scene login;

    public void setLogin(Scene login) {
        this.login = login;
    }

    GradingSystem gs;

    public void setGs(GradingSystem gs) {
        this.gs = gs;
    }
}
