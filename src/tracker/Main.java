package tracker;
import tracker.controller.UIManager;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UIManager uiManager = new UIManager(scanner);

        uiManager.manageUI();

    }
}
