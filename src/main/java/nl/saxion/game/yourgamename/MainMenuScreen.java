package nl.saxion.game.yourgamename;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import nl.saxion.gameapp.GameApp;
import nl.saxion.gameapp.screens.ScalableGameScreen;

public class MainMenuScreen extends ScalableGameScreen {


    private static final String TITLE_FONT_NAME = "menu_title_font";
    private static final String MENU_FONT_NAME = "menu_item_font";
    private static final String MUSIC_NAME = "menu_theme_music";
    private static final int CENTER_ADJUSTMENT = -235; // 👈 시각적 중심을 왼쪽으로 30만큼 조정

    public MainMenuScreen() {
        super(1280, 720);
    }

    @Override
    public void show() {

        GameApp.addFont(TITLE_FONT_NAME, "fonts/basic.ttf", 60);
        GameApp.addFont(MENU_FONT_NAME, "fonts/basic.ttf", 30);
        GameApp.addMusic(MUSIC_NAME, "audio/menu_theme.mp3");
        GameApp.playMusic(MUSIC_NAME, true, 0.5f);
    }

    @Override
    public void render(float delta) {
        GameApp.clearScreen();

        GameApp.startSpriteRendering();

        int centerX = (int)getWorldWidth() / 2 + CENTER_ADJUSTMENT; // 👈 조정값 적용
        int yStart = 300;
        int ySpacing = 50;

        // 1. Title Drawing=
        GameApp.drawTextHorizontallyCentered(TITLE_FONT_NAME, "DEUX EX MACHINA", centerX, 375, "red-700");

        // 2. Menu List Drawing
        GameApp.drawTextHorizontallyCentered(MENU_FONT_NAME, "Start Game: Europe \n(Choose your level)", centerX, yStart, "yellow-500");

        // 3. Level
        GameApp.drawTextHorizontallyCentered(MENU_FONT_NAME, "1. VERY HARD", centerX, yStart - 1 * ySpacing, "gray-500");
        GameApp.drawTextHorizontallyCentered(MENU_FONT_NAME, "2. HARD", centerX, yStart - 2 * ySpacing, "gray-500");
        GameApp.drawTextHorizontallyCentered(MENU_FONT_NAME, "3. NORMAL", centerX, yStart - 3 * ySpacing, "gray-500");
        GameApp.drawTextHorizontallyCentered(MENU_FONT_NAME, "4. EASY", centerX, yStart - 4 * ySpacing, "gray-500");

        GameApp.endSpriteRendering();

        // 4. Input Handling
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            GameApp.switchScreen("SideViewScreen");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            GameApp.switchScreen("YourGameScreen");
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void hide() {
        GameApp.disposeFont(TITLE_FONT_NAME);
        GameApp.disposeFont(MENU_FONT_NAME);
        GameApp.stopMusic(MUSIC_NAME);
        GameApp.disposeMusic(MUSIC_NAME);
    }

    @Override
    public void dispose() {}
}