package nl.saxion.game.yourgamename;

import nl.saxion.gameapp.GameApp;

public class Player {
    float x;
    float y;
    float w;
    float h;
    float velocityY = 0;
    boolean isOnGround;
    float TopDownWidth;
    float TopDownHeight;
    float maxHealth = 10;
    float currentHealth = maxHealth;

    public void UpdateHealth(float amount, boolean increase) {
        if (increase) {
            currentHealth += amount;
        } else {
            currentHealth -= amount;
        }

        if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        }
        if (currentHealth < 0) {
            currentHealth = 0;
        }
    }

    public boolean CheckForHit(float RectangleX, float RectangleY, float RectangleW, float RectangleH ) {
        return GameApp.rectOverlap(x, y, w, h, RectangleX, RectangleY, RectangleW, RectangleH);
    }
}
