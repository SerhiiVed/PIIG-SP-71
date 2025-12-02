package nl.saxion.game.yourgamename;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input;
import nl.saxion.gameapp.GameApp;
import nl.saxion.gameapp.screens.ScalableGameScreen;

public class DamageShowcaseScreen extends ScalableGameScreen {
    Player player;
    boolean part = true;
    float velocityY = 0;
    boolean isOnGround;
    public DamageShowcaseScreen() {
        super(1280, 720);
    }
    public static final int PLAYER_SPEED = 600;

    //creates the arraylist that will hold all damageparts
    ArrayList<DamagePart> damageParts = new ArrayList<>();

    @Override
    public void show() {
        GameApp.addTexture("chatGpt", "textures/ChatGPT.png");
        GameApp.addFont("basic", "fonts/basic.ttf", 100);

        player = new Player();
        player.x = GameApp.getWorldWidth() / 2;
        player.y = 0;
        player.w = 100;
        player.h = 100;

        //creates a new damagepart and adds it to the damagepart arraylist
        DamagePart dp = new DamagePart();
        dp.populateInstance(GameApp.getWorldWidth() / 2, 200, 100, 100, 1, player);
        damageParts.add(dp);

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

        if (part) {
            DamagePart dp = new DamagePart();
            dp.populateInstance(100, 100, 100, 100, 1, player);
            damageParts.add(dp);
            part = false;
        }
        //Iterates through all damageparts and checks if the damagepart has hit the player
        Iterator<DamagePart> iter = damageParts.iterator();
        while (iter.hasNext()) {
            DamagePart dp = iter.next();
            if (dp.CheckForPlayer()) {
                iter.remove();
                System.out.println("DamagePart hit player and removed!");
            }
        }

        GameApp.clearScreen();
        player.x = GameApp.clamp(player.x, 0, getWorldWidth() - 100);

        GameApp.startSpriteRendering();
        GameApp.drawTexture("chatGpt", player.x, player.y, player.w, player.h);
        GameApp.drawTextCentered("basic", "HEALTH: " + player.currentHealth, getWorldWidth()/2, getWorldHeight()/2, "lime-600");
        GameApp.endSpriteRendering();

        GameApp.startShapeRenderingFilled();
        //draws all damageparts on the screen
        for (DamagePart dp : damageParts) {
            GameApp.drawRect(dp.x, dp.y, dp.w, dp.h, "red-950");
        }
        GameApp.endShapeRendering();

    }

    @Override
    public void hide() {
        GameApp.disposeTexture("chatGpt");
    }

}
