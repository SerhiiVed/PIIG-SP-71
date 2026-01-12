package nl.saxion.game.yourgamename;

import com.badlogic.gdx.Input;
import nl.saxion.gameapp.GameApp;
import nl.saxion.gameapp.screens.ScalableGameScreen;
import nl.saxion.gameapp.utils.Timer;

import java.util.Iterator;

public class TopDownScreen extends ScalableGameScreen {
    Player PlayerCharacter = new Player();
    Boss BossCharacter = new Boss();
    GlobalArrays Parts = new GlobalArrays();
    int attkCount = 1;
    float attkCooldown = 0f;
    private static final String MUSIC_NAME = "game2_theme_music";

    public TopDownScreen() {
        super(1280, 720);
    }

    @Override
    public void show() {
        GameApp.addSpriteSheet("boss", "textures/bossSpriteSheet.png", 2798, 2160);
        GameApp.addAnimationFromSpritesheet("bossShoot", "boss", 0.25f, true);

        GameApp.addTexture("EuropeBossBG", "textures/europeBossBG.png");
//        GameApp.addTexture("EuropeBoss", "textures/europeBoss.png");

        GameApp.addFont("basic", "fonts/basic.ttf", 50);
        GameApp.addFont("basic2", "fonts/basic.ttf", 200);
        GameApp.addMusic(MUSIC_NAME, "audio/game2_theme.mp3");
        GameApp.playMusic(MUSIC_NAME, true, 0.5f);

        PlayerCharacter.TopDownWidth = 40;
        PlayerCharacter.TopDownHeight = 40;
        PlayerCharacter.x = getWorldWidth() / 2 - PlayerCharacter.TopDownWidth / 2;
        PlayerCharacter.y = 100;

        BossCharacter.w = 480;
        BossCharacter.h = 340;


        BossCharacter.x = getWorldWidth() / 2 - BossCharacter.w / 2;
        BossCharacter.y = getWorldHeight() / 2 - BossCharacter.h / 2;
        BossCharacter.playerToTarget = PlayerCharacter;
        BossCharacter.partArray = Parts;

        GameApp.addTimer("attack", 3f, true);

    }

    @Override
    public void render(float delta) {
        GameApp.updateAnimation("bossShoot");

        super.render(delta);
        GameApp.updateTimers();


        if (PlayerCharacter.currentHealth != 0f && BossCharacter.currentHealth != 0f) {
            float speed = 500;

            if (GameApp.isKeyPressed(Input.Keys.W)) {
                if (!((PlayerCharacter.y + PlayerCharacter.TopDownHeight) >= getWorldHeight())) {
                    PlayerCharacter.y += speed * delta;
                }
            }
            if (GameApp.isKeyPressed(Input.Keys.S)) {
                if (!(PlayerCharacter.y <= 0)) {
                    PlayerCharacter.y -= speed * delta;
                }
            }
            if (GameApp.isKeyPressed(Input.Keys.A)) {
                if (!(PlayerCharacter.x <= 0)) {
                    PlayerCharacter.x -= speed * delta;
                }
            }
            if (GameApp.isKeyPressed(Input.Keys.D)) {
                if (!((PlayerCharacter.x + PlayerCharacter.TopDownWidth) >= getWorldWidth())) {
                    PlayerCharacter.x += speed * delta;
                }
            }

            if (attkCooldown > 0) {
                attkCooldown -= delta;
            }
            if (GameApp.isKeyPressed(Input.Keys.SPACE)) {
                if (attkCooldown <= 0f) {
                    BossCharacter.shootAtSelf();
                    attkCooldown = 0.25f;   // 1 second cooldown
                }
            }

            //Iterates through all damage parts and checks if the damage part has hit the player and cleans out of view parts
            Iterator<Part> iter = Parts.Parts.iterator();
            while (iter.hasNext()) {
                Part dp = iter.next();
                if (dp.x + dp.w < 0 || dp.x > 1600 || dp.y + dp.h < 0 || dp.y > 1200) {
                    iter.remove();
                    continue;
                }

                if (dp.canDamage && (dp.checkForPlayer() || dp.checkForBoss())) {
                    iter.remove();
                }
            }

            for (Part p : Parts.Parts) {
                if (p.canMove) {
                    p.update(delta);
                }
            }

            if (GameApp.timerWentOff("attack")) {
                if (attkCount == 1) {
                    BossCharacter.shootArcTowardPlayer(10, 700, 30);
                    attkCount = 2;
                } else if (attkCount == 2) {
                    BossCharacter.shootCircle(30,600f);
                    attkCount = 3;
                } else if (attkCount == 3) {
                    BossCharacter.shootCircleWithGaps(100, 200f, 30f, 70f);
                    attkCount = 1;
                }
            }
        }



        // Draw elements
        GameApp.clearScreen("black");
        GameApp.startSpriteRendering();
        GameApp.drawTexture("EuropeBossBG", 0, 0, GameApp.getTextureWidth("EuropeBossBG")/3, GameApp.getTextureHeight("EuropeBossBG")/3);
//        GameApp.drawTexture("EuropeBoss", BossCharacter.x, BossCharacter.y, BossCharacter.w, BossCharacter.h);
        GameApp.drawAnimation("bossShoot",  BossCharacter.x, BossCharacter.y,  BossCharacter.w, BossCharacter.h);


        GameApp.drawTextCentered("basic", "Player Health: " + PlayerCharacter.currentHealth, 200, 600, "amber-500");
        GameApp.drawTextCentered("basic", "Boss Health: " + BossCharacter.currentHealth, 1000, 600, "amber-500");

        if (PlayerCharacter.currentHealth == 0f) {
            GameApp.drawTextCentered("basic2", "You Died", getWorldWidth()/2, getWorldHeight()/2, "red-600");

        }
        if (BossCharacter.currentHealth == 0f) {
            GameApp.drawTextCentered("basic2", "You Won", getWorldWidth()/2, getWorldHeight()/2, "green-500");

        }
        GameApp.endSpriteRendering();
        GameApp.startShapeRenderingFilled();
        GameApp.drawRect(PlayerCharacter.x, PlayerCharacter.y, PlayerCharacter.TopDownWidth, PlayerCharacter.TopDownHeight, "black");
//        GameApp.drawRect(BossCharacter.x, BossCharacter.y, BossCharacter.w, BossCharacter.h, "red-500");

        //draws all damage parts on the screen
        for (Part dp : Parts.Parts) {
            GameApp.drawRect(dp.x, dp.y, dp.w, dp.h, dp.color);
        }
        GameApp.endShapeRendering();
    }

    @Override
    public void hide() {

    }
}
