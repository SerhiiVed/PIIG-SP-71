package nl.saxion.game.yourgamename;

public class Part {

    float x, y, w, h;
    float velX = 0, velY = 0;
    float damage = 1f;

    String color = "neutral-50";

    boolean canDamage = false;
    boolean canMove = false;
    boolean ownedByPlayer;


    Player playerInstance;
    Boss bossInstance;

    public Part(float x, float y, float width, float height,
                float damageAmount, Player player, Boss boss, boolean ownedByPlayer,
                boolean canDamage, boolean canMove,
                float velX, float velY)
    {
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;

        this.damage = damageAmount;
        this.playerInstance = player;
        this.bossInstance = boss;
        this.ownedByPlayer = ownedByPlayer;

        this.canDamage = canDamage;
        this.canMove = canMove;

        this.velX = velX;
        this.velY = velY;
    }

    public boolean checkForPlayer() {
        if (playerInstance == null) {
            System.err.println("Warning: playerInstance has not been assigned");
            return false;
        }

        if (canDamage && playerInstance.CheckForHit(x, y, w, h) && !ownedByPlayer) {
            playerInstance.UpdateHealth(damage, false);
            return true;
        }

        return false;
    }

    public boolean checkForBoss() {
        if (bossInstance == null) {
            System.err.println("Warning: bossInstance has not been assigned");
            return false;
        }

        if (canDamage && bossInstance.CheckForHit(x, y, w, h) && ownedByPlayer) {
            bossInstance.UpdateHealth(damage, false);
            return true;
        }

        return false;
    }

    public void update(float delta) {
        if (canMove) {
            x += velX * delta;
            y += velY * delta;
        }
    }
}
