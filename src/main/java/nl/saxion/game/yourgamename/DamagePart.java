package nl.saxion.game.yourgamename;

import javax.xml.parsers.SAXParser;

public class DamagePart {
    float x;
    float y;
    float w;
    float h;
    float damage = 1;
    Player playerInstance;

    public void populateInstance(float X, float Y, float width, float height, float damageAmount, Player pInstance) {
        x = X;
        y = Y;
        w = width;
        h = height;
        damage = damageAmount;
        playerInstance = pInstance;
    }

    public boolean CheckForPlayer() {
        if (playerInstance != null) {
            if (playerInstance.CheckForHit(x,y,w,h)) {
                playerInstance.UpdateHealth(damage, false);
                return true;
            }
        } else {
            System.err.println("Warning: playerInstance has not been assigned");
            return false;
        }
        return false;
    }
}
