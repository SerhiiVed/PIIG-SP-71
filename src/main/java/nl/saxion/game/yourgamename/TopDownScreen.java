package nl.saxion.game.yourgamename;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import nl.saxion.gameapp.GameApp;
import nl.saxion.gameapp.screens.ScalableGameScreen;
import nl.saxion.gameapp.utils.Timer;

import java.util.Iterator;

public class TopDownScreen extends ScalableGameScreen {
    float healthBarX = 20;
    float healthBarY = 20;
    float healthBarsWidth = 300;
    float healthBarsHeight = 50;
    float currentHealthWidth = 300;

    float bossHealthBarX = 400;
    float bossHealthCurrentWidth = 300;

    public static final int PLAYER_SPEED = 600;

    boolean isHyperDriving = false;
    float hyperDriveTimer = 0f;
    final float HYPER_DRIVE_DURATION = 4.0f;
    final float SPEED_MULTIPLIER = 1.2f;
    final float JUMP_MULTIPLIER = 1.2f;

    boolean isviruson = false;
    float virusTimer = 0f;
    final float VIRUS_DURATION = 4.0f;
    final float SPEED_DIVIDER = 0.3f;
    final float JUMP_DIVIDER = 0.3f;

    public static final int ITEM_SIZE= 100;

    private float itemSpawnTimer = 9.0f;
    private float itemActiveTimer = 0f;
    private float itemX, itemY;
    private String currentItemOnField = "";
    private int spawnPhase = 0;

    Player PlayerCharacter = new Player();
    Boss BossCharacter = new Boss();
    GlobalArrays Parts = new GlobalArrays();
    int attkCount = 1;
    float attkCooldown = 0f;
    private static final String MUSIC_NAME = "game2_theme_music";
    private static final String HYPER_SOUND = "hyper_sound";
    private static final String VIRUS_SOUND = "virus_sound";
    private static final String WIN_MUSIC = "win_music";
    private static final String LOSE_MUSIC = "lose_music";
    private boolean isGameOverMusicPlaying = false;
    boolean cutscene = false;
    float fadeIn = 1500;

    public TopDownScreen() {
        super(1280, 720);
    }

    @Override
    public void show() {
        GameApp.addSpriteSheet("boss", "textures/bossSpriteSheet.png", 2798, 2160);
        GameApp.addAnimationFromSpritesheet("bossShoot", "boss", 0.25f, true);

        GameApp.addFont("tech300", "fonts/ShareTech-Regular.ttf", 300);

        GameApp.addTexture("EuropeBossBG", "textures/europeBossBG.png");
//        GameApp.addTexture("EuropeBoss", "textures/europeBoss.png");

        GameApp.addFont("basic", "fonts/basic.ttf", 50);
        GameApp.addFont("basic2", "fonts/basic.ttf", 200);
        GameApp.addMusic(MUSIC_NAME, "audio/game2_theme.mp3");
        GameApp.playMusic(MUSIC_NAME, true, 0.5f);
        GameApp.addTexture("Hyper-drive", "textures/item_upgrade_hyper_drive.png");
        GameApp.addTexture("Virus", "textures/item_downgrade_virus.png");
        GameApp.addSound(HYPER_SOUND, "audio/item_hyper_drive_sound.mp3");
        GameApp.addSound(VIRUS_SOUND, "audio/item_virus_sound.mp3");
        GameApp.addMusic(WIN_MUSIC, "audio/win_music.mp3");
        GameApp.addMusic(LOSE_MUSIC, "audio/lose_music.mp3");

        PlayerCharacter.TopDownWidth = 40;
        PlayerCharacter.TopDownHeight = 40;
        PlayerCharacter.x = getWorldWidth() / 2 - PlayerCharacter.TopDownWidth / 2;
        PlayerCharacter.y = 100;

        BossCharacter.w = 480;
        BossCharacter.h = 340;


        BossCharacter.x = getWorldWidth() / 2 - BossCharacter.w / 2;
        BossCharacter.y = getWorldHeight();
        BossCharacter.playerToTarget = PlayerCharacter;
        BossCharacter.partArray = Parts;

        GameApp.addTimer("attack", 3f, true);

    }

    @Override
    public void render(float delta) {
        GameApp.updateAnimation("bossShoot");

        super.render(delta);
        GameApp.updateTimers();

        handleItemSpawning(delta);

        //Starting cutscene
        if (!cutscene) {
            if (fadeIn > 1) {
                fadeIn -= 7;
            } else {
                BossCharacter.y -= 3;
                if (BossCharacter.y <= getWorldHeight() / 2 - BossCharacter.h / 2) {
                    cutscene = true;
                }
            }
        }

        if (isHyperDriving) {
            hyperDriveTimer -= delta;
            if (hyperDriveTimer <= 0) {
                isHyperDriving = false;
                hyperDriveTimer = 0;
            }
        }
        if (isviruson) {
            virusTimer -= delta;
            if (virusTimer <= 0) {
                isviruson = false;
                virusTimer = 0;
            }
        }

//        Health bar
        float healthSegment = healthBarsWidth / PlayerCharacter.maxHealth;
        currentHealthWidth = healthSegment * PlayerCharacter.currentHealth;

        float bossHealthSegment = healthBarsWidth / BossCharacter.maxHealth;
        bossHealthCurrentWidth = bossHealthSegment * BossCharacter.currentHealth;

        float currentSpeed = PLAYER_SPEED;
        float jumpImpulse = 800;
        if (isHyperDriving) { currentSpeed *= SPEED_MULTIPLIER; jumpImpulse *= JUMP_MULTIPLIER; }
        if (isviruson) { currentSpeed *= SPEED_DIVIDER; jumpImpulse *= JUMP_DIVIDER; }

        if (PlayerCharacter.currentHealth != 0f && BossCharacter.currentHealth != 0f && cutscene) {

            if (GameApp.isKeyPressed(Input.Keys.W)) {
                if (!((PlayerCharacter.y + PlayerCharacter.TopDownHeight) >= getWorldHeight())) {
                    PlayerCharacter.y += currentSpeed * delta;
                }
            }
            if (GameApp.isKeyPressed(Input.Keys.S)) {
                if (!(PlayerCharacter.y <= 0)) {
                    PlayerCharacter.y -= currentSpeed * delta;
                }
            }
            if (GameApp.isKeyPressed(Input.Keys.A)) {
                if (!(PlayerCharacter.x <= 0)) {
                    PlayerCharacter.x -= currentSpeed * delta;
                }
            }
            if (GameApp.isKeyPressed(Input.Keys.D)) {
                if (!((PlayerCharacter.x + PlayerCharacter.TopDownWidth) >= getWorldWidth())) {
                    PlayerCharacter.x += currentSpeed * delta;
                }
            }

            if (attkCooldown > 0) {
                attkCooldown -= delta;
            }
            if (attkCooldown <= 0f) {
                BossCharacter.shootAtSelf();

                float baseCooldown = 0.5f;

                if (isHyperDriving) {
                    attkCooldown = baseCooldown * 0.5f;
                } else if (isviruson) {
                    attkCooldown = baseCooldown * 2.0f;
                } else {
                    attkCooldown = baseCooldown;
                }
            }

//            }

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

        if (!currentItemOnField.equals("") && cutscene) {
            if (GameApp.rectOverlap(PlayerCharacter.x, PlayerCharacter.y, PlayerCharacter.TopDownWidth, PlayerCharacter.TopDownHeight,
                    itemX, itemY, ITEM_SIZE, ITEM_SIZE)) {
                activateItemEffect(currentItemOnField);

                if (spawnPhase == 1) {
                    spawnPhase = 2;
                    itemSpawnTimer = 6.0f;
                } else if (spawnPhase == 3) {
                    spawnPhase = 4;
                }

                currentItemOnField = "";
                itemActiveTimer = 0;
            }
        }

        // Draw elements
        GameApp.clearScreen("black");
        GameApp.startSpriteRendering();
        GameApp.drawTexture("EuropeBossBG", 0, 0, GameApp.getTextureWidth("EuropeBossBG")/3, GameApp.getTextureHeight("EuropeBossBG")/3);
//        GameApp.drawTexture("EuropeBoss", BossCharacter.x, BossCharacter.y, BossCharacter.w, BossCharacter.h);
        GameApp.drawAnimation("bossShoot",  BossCharacter.x, BossCharacter.y,  BossCharacter.w, BossCharacter.h);
        if (!currentItemOnField.equals("")) {
            GameApp.drawTexture(currentItemOnField, itemX, itemY, ITEM_SIZE, ITEM_SIZE);
        }

        if (PlayerCharacter.currentHealth == 0f) {
            GameApp.drawTextCentered("tech300", "You Died :(", getWorldWidth()/2, getWorldHeight()/2, "red-600");

        }
        if (BossCharacter.currentHealth == 0f) {
            GameApp.drawTextCentered("tech300", "!!! You Won !!!", getWorldWidth()/2, getWorldHeight()/2, "green-500");

        }
        GameApp.endSpriteRendering();
        GameApp.startSpriteRendering();

        // (You Died)
        if (PlayerCharacter.currentHealth == 0f) {
            if (!isGameOverMusicPlaying) {
                GameApp.stopMusic(MUSIC_NAME); // 배경음 멈춤
                GameApp.playMusic(LOSE_MUSIC, false, 0.7f);
                isGameOverMusicPlaying = true;
            }
            GameApp.drawTextCentered("tech300", "You Died :(", getWorldWidth()/2, getWorldHeight()/2, "red-600");
        }

        // (You Won)
        if (BossCharacter.currentHealth == 0f) {
            if (!isGameOverMusicPlaying) {
                GameApp.stopMusic(MUSIC_NAME);
                GameApp.playMusic(WIN_MUSIC, false, 0.7f);
                isGameOverMusicPlaying = true;
            }
            GameApp.drawTextCentered("tech300", "!!! You Won !!!", getWorldWidth()/2, getWorldHeight()/2, "green-500");
        }

        GameApp.endSpriteRendering();

        GameApp.startShapeRenderingFilled();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        GameApp.drawRect(PlayerCharacter.x, PlayerCharacter.y, PlayerCharacter.TopDownWidth, PlayerCharacter.TopDownHeight, "black");
        GameApp.drawRect(healthBarX, GameApp.getWorldHeight() - 80, healthBarsWidth, healthBarsHeight, Color.RED);
        GameApp.drawRect(healthBarX, GameApp.getWorldHeight() - 80, currentHealthWidth, healthBarsHeight, Color.GREEN);
        GameApp.drawRect(GameApp.getWorldWidth() - bossHealthBarX, GameApp.getWorldHeight() - 80, healthBarsWidth, healthBarsHeight, Color.RED);
        GameApp.drawRect(GameApp.getWorldWidth() - bossHealthBarX, GameApp.getWorldHeight() - 80, bossHealthCurrentWidth, healthBarsHeight, Color.GREEN);

        if (isHyperDriving) {
            GameApp.drawRect(0, 0, getWorldWidth(), getWorldHeight(), new Color(0.6f, 0f, 1f, 0.1f));
            drawGlitches();
        } else if (isviruson) {
            GameApp.drawRect(0, 0, getWorldWidth(), getWorldHeight(), new Color(0f, 1f, 0f, 0.1f));
            drawGlitches();
        }

        if (fadeIn > 1) {
            GameApp.drawRectCentered(getWorldWidth()/2, getWorldHeight()/2, fadeIn, fadeIn, "black");
        }
        for (Part dp : Parts.Parts) {
            GameApp.drawRect(dp.x, dp.y, dp.w, dp.h, dp.color);
        }

        GameApp.endShapeRendering();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void drawGlitches() {
        for (int i = 0; i < 5; i++) {
            float gx = GameApp.random(0, (int)getWorldWidth());
            float gy = GameApp.random(0, (int)getWorldHeight());
            float gw = GameApp.random(50, 400);
            float gh = GameApp.random(2, 10);

            String glitchColor = GameApp.random(0, 1) == 0 ? "white" : "black";
            GameApp.drawRect(gx, gy, gw, gh, glitchColor);
        }
    }

    private void handleItemSpawning(float delta) {
        if (!currentItemOnField.equals("")) {
            itemActiveTimer -= delta;
            if (itemActiveTimer <= 0) {
                if (spawnPhase == 1) {
                    spawnPhase = 2;
                    itemSpawnTimer = 3.0f;
                } else if (spawnPhase == 3) {
                    spawnPhase = 4;
                }
                currentItemOnField = "";
            }
            return;
        }


        if (spawnPhase == 0 || spawnPhase == 2) {
            itemSpawnTimer -= delta;
            if (itemSpawnTimer <= 0) {
                spawnPhase++;
                currentItemOnField = (spawnPhase == 1) ? "Hyper-drive" : "Virus";

                itemX = GameApp.random(100, (int)getWorldWidth() - 200);
                itemY = GameApp.random(100, (int)getWorldHeight() - 200);

                itemActiveTimer = 5.0f;
            }
        }
    }

    private void activateItemEffect(String itemName) {
        if (itemName.equals("Hyper-drive")) {
            isHyperDriving = true;
            hyperDriveTimer = HYPER_DRIVE_DURATION;
            GameApp.playSound(HYPER_SOUND);
        } else if (itemName.equals("Virus")) {
            isviruson = true;
            virusTimer = VIRUS_DURATION;
            GameApp.playSound(VIRUS_SOUND);
        }
    }

    @Override
    public void hide() {
        GameApp.stopMusic(MUSIC_NAME);
        GameApp.disposeMusic(MUSIC_NAME);
        GameApp.disposeSound(HYPER_SOUND);
        GameApp.disposeSound(VIRUS_SOUND);
        GameApp.stopMusic(WIN_MUSIC);
        GameApp.disposeMusic(WIN_MUSIC);
        GameApp.stopMusic(LOSE_MUSIC);
        GameApp.disposeMusic(LOSE_MUSIC);
        GameApp.disposeFont("tech300");
        GameApp.disposeFont("basic");
        GameApp.disposeFont("basic2");
    }
}
