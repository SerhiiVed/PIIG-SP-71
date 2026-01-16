package nl.saxion.game.yourgamename;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import nl.saxion.gameapp.GameApp;
import nl.saxion.gameapp.screens.ScalableGameScreen;

public class MainMenuScreen extends ScalableGameScreen {


    private static final String TITLE_FONT_NAME = "menu_title_font";
    private static final String MENU_FONT_NAME = "menu_item_font";
    private static final String MUSIC_NAME = "menu_theme_music";
    private static final int CENTER_ADJUSTMENT = -235;

    public MainMenuScreen() {
        super(1280, 720);
    }

    @Override
    public void show() {

        GameApp.addTexture("background", "textures/mainMenuBG.png");

        GameApp.addFont(TITLE_FONT_NAME, "fonts/basic.ttf", 60);
        GameApp.addFont(MENU_FONT_NAME, "fonts/basic.ttf", 30);
        GameApp.addMusic(MUSIC_NAME, "audio/menu_theme.mp3");
        GameApp.playMusic(MUSIC_NAME, true, 0.5f);
    }

    @Override
    public void render(float delta) {
        GameApp.clearScreen();

        GameApp.startSpriteRendering();
        GameApp.drawTexture("background", 0,0, (GameApp.getTextureWidth("background")/5)+30, GameApp.getTextureHeight("background")/5);

        int centerX = (int)getWorldWidth() / 2 + CENTER_ADJUSTMENT;
        int yStart = 300;
        int ySpacing = 50;

         // 3. Level
//        GameApp.drawTextHorizontallyCentered(MENU_FONT_NAME, "1. SIDEVIEW", centerX, yStart - 1 * ySpacing, "gray-500");
//        GameApp.drawTextHorizontallyCentered(MENU_FONT_NAME, "2. TOPDOWN", centerX, yStart - 2 * ySpacing, "gray-500");

        GameApp.endSpriteRendering();

        // 4. Input Handling
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            GameApp.switchScreen("TopDownScreen");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            GameApp.switchScreen("TopDownScreen");
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
        GameApp.disposeTexture("background");
    }

    @Override
    public void dispose() {}
}