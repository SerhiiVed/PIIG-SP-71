package nl.saxion.game;

import nl.saxion.game.yourgamename.DamageShowcaseScreen;
import nl.saxion.game.yourgamename.SideViewScreen;
import nl.saxion.game.yourgamename.YourGameScreen;
import nl.saxion.game.yourgamename.MainMenuScreen;
import nl.saxion.game.yourgamename.TopDownScreen;
import nl.saxion.gameapp.GameApp;

public class Main {
    public static void main(String[] args) {
        // Add screens
        GameApp.addScreen("MainMenuScreen", new MainMenuScreen());
        GameApp.addScreen("YourGameScreen", new YourGameScreen());
        GameApp.addScreen("SideViewScreen", new SideViewScreen());
        GameApp.addScreen("DamageShowcaseScreen", new DamageShowcaseScreen());
        GameApp.addScreen("TopDownScreen", new TopDownScreen());

        // Start game loop and show main menu screen
        GameApp.start("MainMenuScreen", 800, 450, 60, false, "SideViewScreen");
    }
}
