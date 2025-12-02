package nl.saxion.game.yourgamename;

import nl.saxion.gameapp.GameApp;

public class Obstacle {
    int width;
    int height;
    int x;
    int y;
    Player playerInstance;

    public void playerMovement (float oldPosY, float oldPosX) {
        if (GameApp.rectOverlap(playerInstance.x, playerInstance.y, playerInstance.w, playerInstance.h,
                x, y, width, height)) {
            if (oldPosY < y + height && oldPosY + playerInstance.h > y) {
                if (oldPosX + playerInstance.w <= x) {
                    playerInstance.x = x - playerInstance.w;
                } else if (oldPosX >= x + width) {
                    playerInstance.x = x + width;
                }
            }
        }

        if (GameApp.rectOverlap(playerInstance.x, playerInstance.y, 100, 100, x, y, width, height)) {
            if (oldPosY >= y + height) {
                playerInstance.y = y + height;
                playerInstance.velocityY = 0;
                playerInstance.isOnGround = true;
            } else if (oldPosY + playerInstance.h <= y) {
                playerInstance.y = y - playerInstance.h;
                playerInstance.velocityY = 0;
            }
        }
    }
}
