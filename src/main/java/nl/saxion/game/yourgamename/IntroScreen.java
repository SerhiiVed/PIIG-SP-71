package nl.saxion.game.yourgamename;

import com.badlogic.gdx.Input;
import nl.saxion.gameapp.GameApp;
import nl.saxion.gameapp.screens.ScalableGameScreen;

public class IntroScreen extends ScalableGameScreen {
    private float stateTime = 0;
    private final float TOTAL_DURATION = 20.0f;
    private final int TOTAL_IMAGES = 80;
    private final float TIME_PER_IMAGE = 0.25f;
    private static final String MUSIC_NAME = "introScreen_theme_music";

    public IntroScreen() {
        super(800, 450);
    }

    @Override
    public void show() {
        for (int i = 1; i <= TOTAL_IMAGES; i++) {
            GameApp.addTexture("intro_bg_" + i, "textures/introScreen_001-080/ezgif-frame-0" + i + ".jpg");
        }
        GameApp.addMusic(MUSIC_NAME, "audio/introScreen_theme_20s.mp3");
        GameApp.playMusic(MUSIC_NAME, true, 0.5f);
    }

    @Override
    public void render(float delta) {
        stateTime += delta;
        GameApp.clearScreen();

        int imageIndex = (int) (stateTime / TIME_PER_IMAGE);
        if (imageIndex >= TOTAL_IMAGES) imageIndex = TOTAL_IMAGES - 1;

        String currentBG = "intro_bg_" + (imageIndex + 1);

        GameApp.startSpriteRendering();
        GameApp.drawTexture(currentBG, 0, 0, getWorldWidth(), getWorldHeight());
        GameApp.endSpriteRendering();

        if (stateTime >= TOTAL_DURATION || GameApp.isKeyPressed(Input.Keys.ENTER)) {
            GameApp.switchScreen("SideViewScreen");
        }
    }

    @Override
    public void hide() {
        for (int i = 1; i <= TOTAL_IMAGES; i++) {
            GameApp.disposeTexture("intro_bg_" + i);
        }

        GameApp.stopMusic(MUSIC_NAME);
        GameApp.disposeMusic(MUSIC_NAME);
    }
}