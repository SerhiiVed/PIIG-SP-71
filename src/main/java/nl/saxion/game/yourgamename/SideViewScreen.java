package nl.saxion.game.yourgamename;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import nl.saxion.gameapp.GameApp;
import nl.saxion.gameapp.screens.ScalableGameScreen;

public class SideViewScreen extends ScalableGameScreen {
    Player player;
    Obstacle obstacle;
    Obstacle obstacle2;
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

        obstacle2 = new Obstacle();
        obstacle2.height = 50;
        obstacle2.width = 200;
        obstacle2.x = 500;
        obstacle2.y = 0;

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

        playerMovement(oldPosY, oldPosX, obstacle.x, obstacle.y, obstacle.height, obstacle.width);
        playerMovement(oldPosY, oldPosX, obstacle2.x, obstacle2.y, obstacle2.height, obstacle2.width);

        GameApp.clearScreen();

        GameApp.startShapeRenderingFilled();
        GameApp.drawRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        GameApp.drawRect(obstacle2.x, obstacle2.y, obstacle2.width, obstacle2.height);
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

    public void playerMovement (float oldPosY, float oldPosX, int obsX, int obsY, int obsH, int obsW) {
        if (GameApp.rectOverlap(player.x, player.y, player.width, player.height,
                obsX, obsY, obsW, obsH)) {
            if (oldPosY < obsY + obsH && oldPosY + player.height > obsY) {
                if (oldPosX + player.width <= obsX) {
                    player.x = obsX - player.width;
                } else if (oldPosX >= obsX + obsW) {
                    player.x = obsX + obsW;
                }
            }
        }

        if (GameApp.rectOverlap(player.x, player.y, 100, 100, obsX, obsY, obsW, obsH)) {
            if (oldPosY >= obsY + obsH) {
                player.y = obsY + obsH;
                velocityY = 0;
                isOnGround = true;
            } else if (oldPosY + player.height <= obsY) {
                player.y = obsY - player.height;
                velocityY = 0;
            }
        }
    }

}
