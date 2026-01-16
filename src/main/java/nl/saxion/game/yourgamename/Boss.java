package nl.saxion.game.yourgamename;

import nl.saxion.gameapp.GameApp;

public class Boss {
    float x;
    float y;
    float w;
    float h;
    float maxHealth = 50;
    float currentHealth = maxHealth;
    Player playerToTarget;
    GlobalArrays partArray;

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

    public void shootAtSelf() {
        float dx = (x + w / 2f) - playerToTarget.x;
        float dy = (y + h / 2f) - playerToTarget.y;

        float length = (float) Math.sqrt(dx*dx + dy*dy);
        float dirX = dx / length;
        float dirY = dy / length;

        float speed = 1000f;

        float velX = dirX * speed;
        float velY = dirY * speed;

//        bullets.add(new Bullet(x, y, velX, velY));
        partArray.Parts.add(new Part(playerToTarget.x + playerToTarget.w / 2f, playerToTarget.y + playerToTarget.h / 2f, 10, 10, 1f, playerToTarget, this, true, true, true, velX, velY));
    }



    public void shootAtPlayer() {
        float dx = (playerToTarget.x + playerToTarget.w / 2f) - x;
        float dy = (playerToTarget.y + playerToTarget.h / 2f) - y;
        float length = (float) Math.sqrt(dx*dx + dy*dy);
        float dirX = dx / length;
        float dirY = dy / length;

        float speed = 1000f;

        float velX = dirX * speed;
        float velY = dirY * speed;

//        bullets.add(new Bullet(x, y, velX, velY));
        partArray.Parts.add(new Part(x, y, 10, 10, 1f, playerToTarget, this, false, true, true, velX, velY));
    }



    public void shootCircle(int bulletCount, float speed) {
        float cx = x + w / 2f;   // center of the boss
        float cy = y + h / 2f;

        for (int i = 0; i < bulletCount; i++) {

            // angle in radians
            float angle = (float) (2 * Math.PI * i / bulletCount);

            // direction vector
            float dirX = (float) Math.cos(angle);
            float dirY = (float) Math.sin(angle);

            // final velocity
            float velX = dirX * speed;
            float velY = dirY * speed;

            // spawn bullet
            partArray.Parts.add(
                    new Part(cx, cy, 10, 10, 1, playerToTarget, this, false, true, true, velX, velY)
            );
        }
    }

    public void shootArcTowardPlayer(int bulletCount, float speed,  float arcWidthDeg) {
        float cx = x + w / 2f;
        float cy = y + h / 2f;

        // angle from boss → player
        float dx = (playerToTarget.x + playerToTarget.w / 2f) - cx;
        float dy = (playerToTarget.y + playerToTarget.h / 2f) - cy;
        float baseAngleDeg = (float) Math.toDegrees(Math.atan2(dy, dx));

        // half the arc width
        float half = arcWidthDeg / 2f;

        float startAngle = baseAngleDeg - half;
        float endAngle   = baseAngleDeg + half;

        for (int i = 0; i < bulletCount; i++) {

            float t = i / (float)(bulletCount - 1);  // 0 to 1
            float angleDeg = startAngle + t * (endAngle - startAngle);

            float angleRad = (float) Math.toRadians(angleDeg);

            float dirX = (float) Math.cos(angleRad);
            float dirY = (float) Math.sin(angleRad);

            float velX = dirX * speed;
            float velY = dirY * speed;

            partArray.Parts.add(
                    new Part(cx, cy, 10, 10, 1, playerToTarget, this, false, true, true, velX, velY)
            );
        }
    }

    public void shootCircleWithGaps(int bulletCount, float speed, float gapStartDeg1, float gapEndDeg1, float gapStartDeg2, float gapEndDeg2) {

        float cx = x + w / 2f;
        float cy = y + h / 2f;

        for (int i = 0; i < bulletCount; i++) {

            float angleDeg = (360f * i / bulletCount);

            // Skip bullets inside the gap
            if (angleDeg >= gapStartDeg1 && angleDeg <= gapEndDeg1) {
                continue;   // Do NOT shoot this angle
            } else if (angleDeg >= gapStartDeg2 && angleDeg <= gapEndDeg2) {
                continue;
            }

            float angleRad = (float) Math.toRadians(angleDeg);
            float dirX = (float) Math.cos(angleRad);
            float dirY = (float) Math.sin(angleRad);

            float velX = dirX * speed;
            float velY = dirY * speed;

            partArray.Parts.add(
                    new Part(cx, cy, 10, 10, 1, playerToTarget, this, false, true, true, velX, velY)
            );
        }
    }



}