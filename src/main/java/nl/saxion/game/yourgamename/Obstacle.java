package nl.saxion.game.yourgamename;

import nl.saxion.gameapp.GameApp;

public class Obstacle {
    int w;
    int h;
    int x;
    int y;
    Player playerInstance;

    public void populateInstance(int X, int Y, int width, int height) {
        x = X;
        y = Y;
        w = width;
        h = height;
    }

    public void playerMovement (float oldPosY, float oldPosX) {
        if (GameApp.rectOverlap(playerInstance.x, playerInstance.y, playerInstance.w, playerInstance.h,
                x, y, w, h)) {
            if (oldPosY < y + h && oldPosY + playerInstance.h > y) {
                if (oldPosX + playerInstance.w <= x) {
                    playerInstance.x = x - playerInstance.w;
                } else if (oldPosX >= x + w) {
                    playerInstance.x = x + w;
                }
            }
        }

        if (GameApp.rectOverlap(playerInstance.x, playerInstance.y, 100, 100, x, y, w, h)) {
            if (oldPosY >= y + h) {
                playerInstance.y = y + h;
                playerInstance.velocityY = 0;
                playerInstance.isOnGround = true;
            } else if (oldPosY + playerInstance.h <= y) {
                playerInstance.y = y - playerInstance.h;
                playerInstance.velocityY = 0;
            }
        }
    }
}
