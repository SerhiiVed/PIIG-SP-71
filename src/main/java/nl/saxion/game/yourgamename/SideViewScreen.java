package nl.saxion.game.yourgamename;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import nl.saxion.gameapp.GameApp;
import nl.saxion.gameapp.screens.ScalableGameScreen;

public class SideViewScreen extends ScalableGameScreen {
    Player player;
    Obstacle obstacle;
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
        player.x = 0;
        player.y = 0;
        player.height = 100;
        player.width = 100;

        obstacle = new Obstacle();
        obstacle.height = 20;
        obstacle.width = 100;
        obstacle.x = 200;
        obstacle.y = 120;

    }

    @Override
    public void render(float delta) {
        float oldPosX = player.x;
        float oldPosY = player.y;
        super.render(delta);

        int gravity = 2000;

        if (GameApp.isKeyPressed(Input.Keys.A)) {
            player.x -= PLAYER_SPEED * delta;
        } if (GameApp.isKeyPressed(Input.Keys.D)) {
            player.x += PLAYER_SPEED * delta;
        } if (GameApp.isKeyPressed(Input.Keys.SPACE) && isOnGround) {
            velocityY = 1000;
            isOnGround = false;
        }

        velocityY = velocityY - (gravity * delta);
        player.y += velocityY*delta;
        if (player.y <= 0) {
            player.y = 0;
            isOnGround = true;
        }

        if (GameApp.rectOverlap(player.x, player.y, player.width, player.height,
                obstacle.x, obstacle.y, obstacle.width, obstacle.height)) {
            if (oldPosY < obstacle.y + obstacle.height && oldPosY + player.height > obstacle.y) {
                if (oldPosX + player.width <= obstacle.x) {
                    player.x = obstacle.x - player.width;
                } else if (oldPosX >= obstacle.x + obstacle.width) {
                    player.x = obstacle.x + obstacle.width;
                }
            }
        }

        if (GameApp.rectOverlap(player.x, player.y, 100, 100, obstacle.x, obstacle.y, obstacle.width, obstacle.height)) {
            if (oldPosY >= obstacle.y + obstacle.height) {
                player.y = obstacle.y + obstacle.height;
                velocityY = 0;
                isOnGround = true;
            } else if (oldPosY + player.height <= obstacle.y) {
                player.y = obstacle.y - player.height;
                velocityY = 0;
            }
        }

        GameApp.clearScreen();

        GameApp.startShapeRenderingFilled();
        GameApp.drawRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        GameApp.endShapeRendering();
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
