package xyz.hashdog.rdm.ui.controller;

import javafx.scene.text.Font;
import xyz.hashdog.rdm.ui.util.ApplicationUtil;

import java.awt.*;
import java.util.List;

public class MainController {
    public static void main(String[] args) {

        List<String> systemFontNames = ApplicationUtil.getSystemFontNames();
        for (String systemFontName : systemFontNames) {
            System.out.println(systemFontName);
        }

    }
}
