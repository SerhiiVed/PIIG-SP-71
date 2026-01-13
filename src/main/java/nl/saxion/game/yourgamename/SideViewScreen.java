package nl.saxion.game.yourgamename;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import nl.saxion.gameapp.GameApp;
import nl.saxion.gameapp.screens.ScalableGameScreen;
import java. util. ArrayList;

public class SideViewScreen extends ScalableGameScreen {
    Player player;
    ArrayList<Item> inventory = new ArrayList<>();
    ArrayList<Item> worldItems = new ArrayList<>();
    ArrayList<Obstacle> obstacles = new ArrayList<>();
    Obstacle movingObstacle;
    Obstacle movingObstacle1;

    Camera camera;
    public SideViewScreen() {
        super(1280, 720);
    }
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
    public static final int PLAYER_SIZE= 130;
    float bgWidth;
    float bgHeight;
    float bgScale;
    private static final String MUSIC_NAME = "game1_theme_music";
    private static final String HYPER_SOUND = "hyper_sound";
    private static final String VIRUS_SOUND = "virus_sound";
    float endCutscene = 0;

    @Override
    public void show() {
        GameApp.addFont("tech100", "fonts/ShareTech-Regular.ttf", 100);
        GameApp.addFont("tech50", "fonts/ShareTech-Regular.ttf", 70);

        GameApp.addTexture("bush", "textures/bushObstacle.png");
        GameApp.addTexture("hand", "textures/handObstacle.png");
        GameApp.addTexture("head", "textures/headObstacle.png");
        GameApp.addTexture("car", "textures/carObstacle.png");
        GameApp.addTexture("tire1", "textures/tire1Obstacle.png");
        GameApp.addTexture("tire2", "textures/tire2Obstacle.png");
        GameApp.addTexture("trash", "textures/Trash.png");
        GameApp.addTexture("cargo", "textures/LongCargo.png");

        GameApp.addTexture("EuropeBG", "textures/EuropeBG.PNG");
        GameApp.addSpriteSheet("characterEurope", "textures/characterEurope.png", 500, 650);
        GameApp.addAnimationFromSpritesheet("characterWalk", "characterEurope", 0.25f, true);

        GameApp.addTexture("item", "textures/item.PNG");
        GameApp.addTexture("Hyper-drive", "textures/item_upgrade_hyper_drive.png");
        GameApp.addTexture("Virus", "textures/item_downgrade_virus.png");
        GameApp.addFont("basicFont", "fonts/basic.ttf", 60);
        GameApp.addMusic(MUSIC_NAME, "audio/game1_theme.mp3");
        GameApp.playMusic(MUSIC_NAME, true, 0.5f);
        GameApp.addSound(HYPER_SOUND, "audio/item_hyper_drive_sound.mp3");
        GameApp.addSound(VIRUS_SOUND, "audio/item_virus_sound.mp3");

         bgWidth = GameApp.getTextureWidth("EuropeBG");
         bgHeight = GameApp.getTextureHeight("EuropeBG");
         bgScale = Math.max(
                getWorldWidth() / bgWidth,
                getWorldHeight() / bgHeight);

        movingObstacle = new Obstacle();
        camera = new Camera();
        player = new Player();
        player.x = 0;
        player.y = 0;
        player.h = 100;
        player.w = 100;

        movingObstacle = new Obstacle();
        movingObstacle.populateInstance(3750, 520, 120,20, player);
        movingObstacle.isMoving = true;
        movingObstacle.maxX = 4150;
        movingObstacle.minX = 3700;
        movingObstacle.texture = "cargo";

        movingObstacle1 = new Obstacle();
        movingObstacle1.populateInstance(6650, 320, 120,20, player);
        movingObstacle1.isMoving = true;
        movingObstacle1.maxX = 7450;
        movingObstacle1.minX = 6750;
        movingObstacle1.texture = "cargo";

        addObstacle("bush",600, 0, 70, 80);        /* bush */
        addObstacle("tire1", 900, 0, 100, 100);      /* tires */
        addObstacle("car", 1200, 0, 270, 80);      /* car */
        addObstacle("trash",2000, 0, 120, 100);     /* trash bin */
        addObstacle("cargo",1700, 220, 120, 20);    /* crane's platform */
        addObstacle("head",2450, 0, 350, 450);     /* head of robot */
        addObstacle("cargo",1900, 360, 120, 20);    /* crane's platform */

        addObstacle("cargo",3300, 180, 120, 20);    /* crane's platform */
        addObstacle("cargo",3600, 320, 120, 20);    /* crane's platform */
        addObstacle("cargo",3400, 470, 120, 20);    /* crane's platform */

        addObstacle("tire2",4100, 0, 100, 100);     /* tires */
        addObstacle("cargo",3870, 220, 120, 20);    /* crane's platform */
        addObstacle("hand",4400, 0, 350, 450);     /* huge hand */

        addObstacle("cargo",5100, 200, 120, 20);    /* crane's platform */
        addObstacle("cargo",5400, 300, 120, 20);    /* crane's platform */
        addObstacle("trash",5700, 0, 120, 100);     /* trash bin */
        addObstacle("car", 6100, 0, 270, 80);      /* car */

        addObstacle("cargo",6580, 180, 120, 20);    /* crane's platform */
        addObstacle("cargo",6800, 460, 120, 20);    /* crane's platform */
        addObstacle("cargo",6400, 550, 120, 20);    /* crane's platform with some item on top */
        addObstacle("cargo",7600, 450, 120, 20);    /* crane's platform */
        addObstacle("head",8000, 0, 350, 450);     /* head of robot */

        inventory.clear();
        worldItems.clear();


        // 1. First item: on the crane's platform(X=1700, Y=220)
        Item hyperDrive2 = new Item();
        hyperDrive2.name = "Hyper-drive";
        hyperDrive2.x = 1710; // calculation: 1700 + (120/2) - (100/2)
        hyperDrive2.y = 240;  // on the obstacle : (220+20)
        hyperDrive2.amount = 1;
        worldItems.add(hyperDrive2);

        // 2. Second item: on the trash bin (X=5700, Y=100)
        Item hyperDrive3 = new Item();
        hyperDrive3.name = "Hyper-drive";
        hyperDrive3.x = 5710; // calculation: 5700 + (120/2) - (100/2)
        hyperDrive3.y = 100;  // on the obstacle : (100)
        hyperDrive3.amount = 1;
        worldItems.add(hyperDrive3);

        // 3. Third item: on the head of robot (X=2450, Y=450)
        Item hyperDrive4 = new Item();
        hyperDrive4.name = "Virus";
        hyperDrive4.x = 2600; // calculation: 2450 + (350/2) - (450/2) + 200
        hyperDrive4.y = 450;  // on the obstacle : (450)
        hyperDrive4.amount = 1;
        worldItems.add(hyperDrive4);

        // 4. Fourth item: on the Huge Hand (X=4550, Y=450)
        Item hyperDrive5 = new Item();
        hyperDrive5.name = "Virus";
        hyperDrive5.x = 4550; // calculation: 4400 + (350/2) - (450/2) + 200
        hyperDrive5.y = 450;  // on the obstacle : (450)
        hyperDrive5.amount = 1;
        worldItems.add(hyperDrive5);

    }

    @Override
    public void render(float delta) {
        GameApp.updateAnimation("characterWalk");

        float oldPosX = player.x;
        float oldPosY = player.y;
        super.render(delta);

        int gravity = 2000;
        player.x = GameApp.clamp(player.x, 0, 10000);
        GameApp.clearScreen();

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

        float currentSpeed = PLAYER_SPEED;
        float jumpImpulse = 800;
        if (isHyperDriving) { currentSpeed *= SPEED_MULTIPLIER; jumpImpulse *= JUMP_MULTIPLIER; }
        if (isviruson) { currentSpeed *= SPEED_DIVIDER; jumpImpulse *= JUMP_DIVIDER; }

        if (GameApp.isKeyPressed(Input.Keys.A)) player.x -= currentSpeed * delta;
        if (GameApp.isKeyPressed(Input.Keys.D)) player.x += currentSpeed * delta;
        if (GameApp.isKeyPressed(Input.Keys.SPACE) && player.isOnGround) {
            player.velocityY = jumpImpulse;
            player.isOnGround = false;
        }
        if (player.velocityY < 0) player.isOnGround = false;

        movePlatform(movingObstacle, movingObstacle.maxX, movingObstacle.minX);
        movePlatform(movingObstacle1, movingObstacle1.maxX, movingObstacle1.minX);

        if (player.x > camera.cameraX + camera.cameraTriggerF) {
            camera.cameraX = player.x - camera.cameraTriggerF;
        } else if (player.x < camera.cameraX + camera.cameraTriggerB) {
            camera.cameraX = player.x - camera.cameraTriggerB;
        }

        player.velocityY = player.velocityY - (gravity * delta);
        player.y += player.velocityY * delta;
        if (player.y <= 0) { player.y = 0; player.isOnGround = true; }

        for (Item item : worldItems) {
            if (!item.collected && GameApp.rectOverlap(player.x, player.y, PLAYER_SIZE, PLAYER_SIZE, item.x, item.y, ITEM_SIZE, ITEM_SIZE)) {
                collectItem(item);
            }
        }
        for (Obstacle ob : obstacles) {
            ob.playerMovement(oldPosY, oldPosX);
            movingObstacle.playerMovement(oldPosY, oldPosX);
            movingObstacle1.playerMovement(oldPosY, oldPosX);
        }

        if (player.x >= 8300.0 && endCutscene == 0) endCutscene = 1;

        GameApp.startSpriteRendering();
        float drawWidth = bgWidth * bgScale;
        float drawHeight = bgHeight * bgScale;
        GameApp.drawTexture("EuropeBG", -300 - camera.cameraX, 0, drawWidth, drawHeight);

        for (Obstacle ob : obstacles) {
            GameApp.drawTexture(ob.texture, ob.x - camera.cameraX, ob.y, ob.w, ob.texture.equals("cargo") ? 600 : ob.h);
        }
        GameApp.drawTexture(movingObstacle.texture, movingObstacle.x - camera.cameraX, movingObstacle.y, movingObstacle.w, 600);
        GameApp.drawTexture(movingObstacle1.texture, movingObstacle1.x - camera.cameraX, movingObstacle1.y, movingObstacle1.w, 600);
        GameApp.drawAnimation("characterWalk", player.x - camera.cameraX, player.y, PLAYER_SIZE, PLAYER_SIZE);

        for (Item item : worldItems) {
            if (!item.collected) {
                GameApp.drawTexture(item.name, item.x - camera.cameraX, item.y, ITEM_SIZE, ITEM_SIZE);
            }
        }

        int invY = 600;
        GameApp.drawText("tech100", "Inventory", 50, invY, Color.WHITE);
        for (Item i : inventory) {
            invY -= 35;
            GameApp.drawText("tech50", i.name + " x" + i.amount, 50, invY, Color.WHITE);
        }
        GameApp.endSpriteRendering();
        GameApp.startShapeRenderingFilled();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        if (isHyperDriving) {
            GameApp.drawRect(0, 0, getWorldWidth(), getWorldHeight(), new Color(0.6f, 0f, 1f, 0.1f));
            drawGlitches();
        } else if (isviruson) {
            GameApp.drawRect(0, 0, getWorldWidth(), getWorldHeight(), new Color(0f, 1f, 0f, 0.1f));
            drawGlitches();
        }

        if (endCutscene >= 1) {
            endCutscene += 7;
            GameApp.drawRectCentered(getWorldWidth()/2, getWorldHeight()/2, endCutscene, endCutscene, "black");
            if (endCutscene > 1500) {
                GameApp.switchScreen("TopDownScreen");
            }
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

    @Override
    public void hide() {
        GameApp.disposeTexture("item");
        GameApp.disposeFont("basicFont");
        GameApp.disposeFont("tech50");
        GameApp.disposeFont("tech100");
        GameApp.stopMusic(MUSIC_NAME);
        GameApp.disposeMusic(MUSIC_NAME);
        GameApp.disposeTexture("EuropeBG");
        GameApp.disposeSpritesheet("characterEurope");
        GameApp.disposeAnimation("characterWalk");
        GameApp.disposeSound(HYPER_SOUND);
        GameApp.disposeSound(VIRUS_SOUND);

    }

//    Custom methods
//
public void collectItem(Item collectedItem) {
    collectedItem.collected = true;
    if (collectedItem.name.equals("Hyper-drive")) {
        isHyperDriving = true;
        hyperDriveTimer = HYPER_DRIVE_DURATION;
        GameApp.playSound(HYPER_SOUND);
    } else if (collectedItem.name.equals("Virus")) {
        isviruson = true;
        virusTimer = VIRUS_DURATION;
        GameApp.playSound(VIRUS_SOUND);
    }
    for (Item i : inventory) {
        if (i.name.equals(collectedItem.name)) {
            i.amount++;
            return;
        }
    }

    Item newItem = new Item();
    newItem.name = collectedItem.name;
    newItem.amount = 1;
    inventory.add(newItem);
}


    public void movePlatform (Obstacle ob, int x, int y) {

        if (ob.isMovingRight) {
            ob.x += 2;
            if (ob.x >= x) {
                ob.isMovingRight = false;
            }
        } else {
            ob.x -= 2;
            if (ob.x <= y) {
                ob.isMovingRight = true;
            }
        }
    }

    public void addObstacle (String texture, int x, int y, int w, int h) {
        Obstacle item = new Obstacle();
        item.texture=texture;
        item.populateInstance(x, y, w, h, player);
        obstacles.add(item);
    }
}