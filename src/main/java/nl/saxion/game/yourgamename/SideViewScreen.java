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

    Camera camera;
//    float velocityY = 0;
//    boolean isOnGround;
    public SideViewScreen() {
        super(1280, 720);
    }
    public static final int PLAYER_SPEED = 600;
    public static final int ITEM_SIZE= 100;
    public static final int PLAYER_SIZE= 130;
    float bgWidth;
    float bgHeight;
    float bgScale;
    public static final int PLAYER_SIZE= 100;
    private static final String MUSIC_NAME = "game1_theme_music";
    public boolean isMovingRight = true;

    @Override
    public void show() {
        GameApp.addTexture("EuropeBG", "textures/EuropeBG.PNG");

        GameApp.addSpriteSheet("characterEurope", "textures/characterEurope.png", 500, 650);
        GameApp.addAnimationFromSpritesheet("characterWalk", "characterEurope", 0.25f, true);

        GameApp.addTexture("chatGpt", "textures/ChatGPT.png");
        GameApp.addTexture("item", "textures/item.PNG");
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

        movingObstacle.x = 3750;
        movingObstacle.y = 520;
        movingObstacle.h = 20;
        movingObstacle.w = 120;
        movingObstacle.playerInstance = player;

        addObstacle(600, 0, 70, 80);
        addObstacle(900, 0, 100, 100);
        addObstacle(1200, 0, 270, 80);
        addObstacle(2000, 0, 120, 100);
        addObstacle(1700, 220, 120, 20);
        addObstacle(2450, 0, 350, 450);
        addObstacle(1900, 360, 120, 20);

        addObstacle(3300, 180, 120, 20);
        addObstacle(3600, 320, 120, 20);
        addObstacle(3400, 470, 120, 20);

        addObstacle(3900, 0, 100, 100);

        inventory.clear();
        worldItems.clear();

        for (int i = 0; i < 10; i++) {
            Item coin = new Item();
            coin.name = "Coin";
            coin.x = GameApp.random(0, getWorldWidth() - ITEM_SIZE);
            coin.y = GameApp.random(0,100);
            coin.amount = 1;

            worldItems.add(coin);
        }

    }

    @Override
    public void render(float delta) {
        GameApp.updateAnimation("characterWalk");

        float oldPosX = player.x;
        float oldPosY = player.y;
        super.render(delta);

        int gravity = 2000;
        player.x = GameApp.clamp(player.x, 0, 10000);


//          Player movement
        if (GameApp.isKeyPressed(Input.Keys.A)) {
            player.x -= PLAYER_SPEED * delta;
        } if (GameApp.isKeyPressed(Input.Keys.D)) {
            player.x += PLAYER_SPEED * delta;
        } if (GameApp.isKeyPressed(Input.Keys.SPACE) && player.isOnGround) {
            player.velocityY = 800;
            player.isOnGround = false;
        } if (player.velocityY < 0) {
            player.isOnGround = false;
        }

        movePlatform();

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

        for (Item item : worldItems) {
            if (!item.collected && GameApp.rectOverlap( player.x, player.y, PLAYER_SIZE, PLAYER_SIZE, item.x, item.y, ITEM_SIZE, ITEM_SIZE)) {
                collectItem(item);
            }
        }
        for (Obstacle ob: obstacles) {
            ob.playerMovement(oldPosY, oldPosX);
        }
        movingObstacle.playerMovement(oldPosY, oldPosX);

        GameApp.clearScreen();

        float drawWidth = bgWidth * bgScale;
        float drawHeight = bgHeight * bgScale;

        float xBg = (getWorldWidth() - drawWidth) / 2;
        float yBg = (getWorldHeight() - drawHeight) / 2;


        GameApp.startSpriteRendering();
        GameApp.drawTexture("EuropeBG", xBg, yBg, drawWidth,  drawHeight );
        GameApp.endSpriteRendering();


        GameApp.startShapeRenderingFilled();
            for (Obstacle ob: obstacles) {
                GameApp.drawRect(ob.x - camera.cameraX, ob.y, ob.w, ob.h);
            }
            GameApp.drawRect(movingObstacle.x - camera.cameraX, movingObstacle.y, movingObstacle.w, movingObstacle.h);
        GameApp.endShapeRendering();

        player.x = GameApp.clamp(player.x, 0, getWorldWidth() - 100);

        GameApp.startSpriteRendering();
        GameApp.drawTexture("chatGpt", player.x - camera.cameraX, player.y, PLAYER_SIZE, PLAYER_SIZE);
        for (Item item : worldItems) {
            if (!item.collected) {
                GameApp.drawTexture("item", item.x  - camera.cameraX, item.y, ITEM_SIZE, ITEM_SIZE);
            }
        }
        int y = 550;
        GameApp.drawText("basicFont", "Inventory", 50, y, Color.BLACK);
        y -= 30;

        for (Item i : inventory) {
            GameApp.drawText("basicFont", i.name + " x" + i.amount, 50, y, Color.BLACK);
            y += 22;
        }

        GameApp.endSpriteRendering();

    }

    @Override
    public void hide() {
        GameApp.disposeTexture("chatGpt");
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


    public void movePlatform () {
        if (isMovingRight) {
            movingObstacle.x += 3;
            if (movingObstacle.x >= 4500) {
                isMovingRight = false;
            }
        } else {
            movingObstacle.x -= 3;
            if (movingObstacle.x <= 3700) {
                isMovingRight = true;
            }
        }
    }

    public void addObstacle (int x, int y, int w, int h) {
        Obstacle item = new Obstacle();
        item.x = x;
        item.y = y;
        item.w = w;
        item.h = h;
        item.playerInstance = player;
        obstacles.add(item);
    }
}