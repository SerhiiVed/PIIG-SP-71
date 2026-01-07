package nl.saxion.game.yourgamename;

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

    @Override
    public void show() {
        GameApp.addTexture("EuropeBG", "textures/EuropeBG.PNG");
        GameApp.addSpriteSheet("characterEurope", "textures/characterEurope.png", 500, 650);
        GameApp.addAnimationFromSpritesheet("characterWalk", "characterEurope", 0.25f, true);

        GameApp.addTexture("chatGpt", "textures/ChatGPT.png");
        GameApp.addTexture("item", "textures/item.PNG");
        GameApp.addTexture("upgrade_hyper_drive", "textures/item_upgrade_hyper_drive.png");
        GameApp.addTexture("downgrade_virus", "textures/item_downgrade_virus.png");
        GameApp.addFont("basicFont", "fonts/basic.ttf", 60);
        GameApp.addMusic(MUSIC_NAME, "audio/game1_theme.mp3");
        GameApp.playMusic(MUSIC_NAME, true, 0.5f);

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

        movingObstacle1 = new Obstacle();
        movingObstacle1.populateInstance(6650, 320, 120,20, player);
        movingObstacle1.isMoving = true;
        movingObstacle1.maxX = 7450;
        movingObstacle1.minX = 6750;

        addObstacle(600, 0, 70, 80);        /* bush */
        addObstacle(900, 0, 100, 100);      /* tires */
        addObstacle(1200, 0, 270, 80);      /* car */
        addObstacle(2000, 0, 120, 100);     /* trash bin */
        addObstacle(1700, 220, 120, 20);    /* crane's platform */
        addObstacle(2450, 0, 350, 450);     /* head of robot */
        addObstacle(1900, 360, 120, 20);    /* crane's platform */

        addObstacle(3300, 180, 120, 20);    /* crane's platform */
        addObstacle(3600, 320, 120, 20);    /* crane's platform */
        addObstacle(3400, 470, 120, 20);    /* crane's platform */

        addObstacle(4100, 0, 100, 100);     /* tires */
        addObstacle(3870, 220, 120, 20);    /* crane's platform */
        addObstacle(4400, 0, 350, 450);     /* huge hand */

        addObstacle(5100, 200, 120, 20);    /* crane's platform */
        addObstacle(5400, 300, 120, 20);    /* crane's platform */
        addObstacle(5700, 0, 120, 100);     /* trash bin */
        addObstacle(6100, 0, 270, 80);      /* car */

        addObstacle(6580, 180, 120, 20);    /* crane's platform */
        addObstacle(6800, 460, 120, 20);    /* crane's platform */
        addObstacle(6400, 550, 120, 20);    /* crane's platform with some item on top */
        addObstacle(7600, 450, 120, 20);    /* crane's platform */
        addObstacle(8000, 0, 350, 450);     /* head of robot */


        inventory.clear();
        worldItems.clear();


        // 1. First item: on the crane's platform(X=1700, Y=220)
        Item hyperDrive2 = new Item();
        hyperDrive2.name = "upgrade_hyper_drive";
        hyperDrive2.x = 1710; // calculation: 1700 + (120/2) - (100/2)
        hyperDrive2.y = 240;  // on the obstacle : (220+20)
        hyperDrive2.amount = 1;
        worldItems.add(hyperDrive2);

        // 2. Second item: on the trash bin (X=5700, Y=100)
        Item hyperDrive3 = new Item();
        hyperDrive3.name = "upgrade_hyper_drive";
        hyperDrive3.x = 5710; // calculation: 5700 + (120/2) - (100/2)
        hyperDrive3.y = 100;  // on the obstacle : (100)
        hyperDrive3.amount = 1;
        worldItems.add(hyperDrive3);

        // 3. Third item: on the head of robot (X=2450, Y=450)
        Item hyperDrive4 = new Item();
        hyperDrive4.name = "downgrade_virus";
        hyperDrive4.x = 2600; // calculation: 2450 + (350/2) - (450/2) + 200
        hyperDrive4.y = 450;  // on the obstacle : (450)
        hyperDrive4.amount = 1;
        worldItems.add(hyperDrive4);

        // 4. Fourth item: on the Huge Hand (X=4550, Y=450)
        Item hyperDrive5 = new Item();
        hyperDrive5.name = "downgrade_virus";
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

//          Player movement
        if (isHyperDriving) {
            hyperDriveTimer -= delta; // Timer Decreased for delta time
            if (hyperDriveTimer <= 0) {
                isHyperDriving = false; // Boost done when the time is done
                hyperDriveTimer = 0;
            }
        }

        if (isviruson) {
            virusTimer -= delta; // Timer Decreased for delta time
            if (virusTimer <= 0) {
                isviruson = false; // Boost done when the time is done
                virusTimer = 0;
            }
        }

        float currentSpeed = PLAYER_SPEED;
        float jumpImpulse = 800;

        if (isHyperDriving) {
            currentSpeed *= SPEED_MULTIPLIER;
            jumpImpulse *= JUMP_MULTIPLIER;
        }

        if (isviruson) {
            currentSpeed *= SPEED_DIVIDER;
            jumpImpulse *= JUMP_DIVIDER;
        }

        if (GameApp.isKeyPressed(Input.Keys.A)) {
            player.x -= currentSpeed * delta;
        } if (GameApp.isKeyPressed(Input.Keys.D)) {
            player.x += currentSpeed * delta;
        } if (GameApp.isKeyPressed(Input.Keys.SPACE) && player.isOnGround) {
            player.velocityY = jumpImpulse;
            player.isOnGround = false;
        } if (player.velocityY < 0) {
            player.isOnGround = false;
        }
//        platforms movement
        movePlatform(movingObstacle, movingObstacle.maxX, movingObstacle.minX);
        movePlatform(movingObstacle1, movingObstacle1.maxX, movingObstacle1.minX);

//        Camera (follow player)
        if (player.x > camera.cameraX + camera.cameraTriggerF) {
            camera.cameraX = player.x - camera.cameraTriggerF;
        } else if (player.x < camera.cameraX + camera.cameraTriggerB) {
            camera.cameraX = player.x - camera.cameraTriggerB;
        }
        player.velocityY = player.velocityY - (gravity * delta);
        player.y += player.velocityY*delta;
        if (player.y <= 0) {
            player.y = 0;
            player.isOnGround = true;
        }

//        Collecting items
        for (Item item : worldItems) {
            if (!item.collected && GameApp.rectOverlap( player.x, player.y, PLAYER_SIZE, PLAYER_SIZE, item.x, item.y, ITEM_SIZE, ITEM_SIZE)) {
                collectItem(item);
            }
        }
//        Adding relation of player to obstacle
        for (Obstacle ob: obstacles) {
            ob.playerMovement(oldPosY, oldPosX);
            movingObstacle.playerMovement(oldPosY, oldPosX);
            movingObstacle1.playerMovement(oldPosY, oldPosX);
        }

        float drawWidth = bgWidth * bgScale;
        float drawHeight = bgHeight * bgScale;


        GameApp.startSpriteRendering();
        GameApp.drawTexture("EuropeBG", -300-camera.cameraX, 0, drawWidth,  drawHeight);
        GameApp.endSpriteRendering();

        GameApp.startShapeRenderingFilled();
            for (Obstacle ob: obstacles) {
                GameApp.drawRect(ob.x - camera.cameraX, ob.y, ob.w, ob.h);
            }
            GameApp.drawRect(movingObstacle.x - camera.cameraX, movingObstacle.y, movingObstacle.w, movingObstacle.h);
            GameApp.drawRect(movingObstacle1.x - camera.cameraX, movingObstacle1.y, movingObstacle1.w, movingObstacle1.h);
        GameApp.endShapeRendering();

        GameApp.startSpriteRendering();
            GameApp.drawAnimation("characterWalk", player.x - camera.cameraX, player.y, PLAYER_SIZE, PLAYER_SIZE);

        for (Item item : worldItems) {
            if (!item.collected) {

                String textureId = null;

                if (item.name.equals("upgrade_hyper_drive")) {
                    textureId = "upgrade_hyper_drive";
                }
                else if (item.name.equals("downgrade_virus")) {
                    textureId = "downgrade_virus";
                }
                if (textureId != null) {
                    GameApp.drawTexture(
                            textureId,
                            item.x - camera.cameraX,
                            item.y,
                            ITEM_SIZE,
                            ITEM_SIZE
                    );
                }
            }
        }
            int y = 600;
            GameApp.drawText("basicFont", "Inventory", 50, y, Color.WHITE);
            y -= 50;

        for (Item i : inventory) {
            GameApp.drawText("basicFont", i.name + " x" + i.amount, 50, y, Color.WHITE);
            y -= 35;
        }

        GameApp.endSpriteRendering();

        if (player.x >= 8350.0) {
            GameApp.switchScreen("TopDownScreen");
        }

    }

    @Override
    public void hide() {
        GameApp.disposeTexture("item");
        GameApp.disposeTexture("basicFont");
        GameApp.stopMusic(MUSIC_NAME);
        GameApp.disposeMusic(MUSIC_NAME);
        GameApp.disposeTexture("EuropeBG");
        GameApp.disposeSpritesheet("characterEurope");
        GameApp.disposeAnimation("characterWalk");

    }

//    Custom methods
//
    public void collectItem(Item collectedItem) {
        collectedItem.collected = true;

        if (collectedItem.name.equals("upgrade_hyper_drive")) {
            isHyperDriving = true;
            hyperDriveTimer = HYPER_DRIVE_DURATION;
        }
        if (collectedItem.name.equals("downgrade_virus")) {
            isviruson = true;
            virusTimer = VIRUS_DURATION;
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

    public void addObstacle (int x, int y, int w, int h) {
        Obstacle item = new Obstacle();
        item.populateInstance(x, y, w, h, player);
        obstacles.add(item);
    }
}