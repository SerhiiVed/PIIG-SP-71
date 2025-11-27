package nl.saxion.game.yourgamename;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import nl.saxion.gameapp.GameApp;
import nl.saxion.gameapp.screens.ScalableGameScreen;

public class SideViewScreen extends ScalableGameScreen {
    Player player;
    float velocityY = 0;
    boolean isOnGround;
    public SideViewScreen() {
        super(1280, 720);
    }
    public static final int PLAYER_SPEED = 600;

    @Override
    public void show() {
        GameApp.addTexture("chatGpt", "textures/ChatGPT.png");

        player = new Player();
        player.x = GameApp.getWorldWidth() / 2;
        player.y = 0;
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        int gravity = 2000;

        if (GameApp.isKeyPressed(Input.Keys.A)) {
            player.x -= PLAYER_SPEED * delta;
        } if (GameApp.isKeyPressed(Input.Keys.D)) {
            player.x += PLAYER_SPEED * delta;
        } if (GameApp.isKeyPressed(Input.Keys.SPACE) && isOnGround) {
            velocityY = 800;
            isOnGround = false;
        }

        velocityY = velocityY - (gravity * delta);
        player.y += velocityY*delta;
        if (player.y <= 0) {
            player.y = 0;
            isOnGround = true;
        }

        GameApp.clearScreen();
        player.x = GameApp.clamp(player.x, 0, getWorldWidth() - 100);

        GameApp.startSpriteRendering();
        GameApp.drawTexture("chatGpt", player.x, player.y, 100, 100);
        GameApp.endSpriteRendering();

    }

    @Override
    public void hide() {
        GameApp.disposeTexture("chatGpt");
    }

}
