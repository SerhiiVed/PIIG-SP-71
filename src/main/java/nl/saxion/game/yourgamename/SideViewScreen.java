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
    Obstacle obstacle;
    Obstacle obstacle2;
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

    @Override
    public void show() {
        GameApp.addTexture("EuropeBG", "textures/EuropeBG.PNG");

        GameApp.addSpriteSheet("characterEurope", "textures/characterEurope.png", 500, 650);
        GameApp.addAnimationFromSpritesheet("characterWalk", "characterEurope", 0.25f, true);

        GameApp.addTexture("chatGpt", "textures/ChatGPT.png");
        GameApp.addTexture("item", "textures/item.PNG");
        GameApp.addFont("basicFont", "fonts/basic.ttf", 60);

         bgWidth = GameApp.getTextureWidth("EuropeBG");
         bgHeight = GameApp.getTextureHeight("EuropeBG");
         bgScale = Math.max(
                getWorldWidth() / bgWidth,
                getWorldHeight() / bgHeight);

        player = new Player();
        player.x = 0;
        player.y = 0;
        player.h = 100;
        player.w = 100;

        obstacle = new Obstacle();
        obstacle.height = 20;
        obstacle.width = 100;
        obstacle.x = 200;
        obstacle.y = 120;
        obstacle.playerInstance = player;

        obstacle2 = new Obstacle();
        obstacle2.height = 50;
        obstacle2.width = 200;
        obstacle2.x = 500;
        obstacle2.y = 0;
        obstacle2.playerInstance = player;


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

        if (GameApp.isKeyPressed(Input.Keys.A)) {
            player.x -= PLAYER_SPEED * delta;
        } if (GameApp.isKeyPressed(Input.Keys.D)) {
            player.x += PLAYER_SPEED * delta;
        } if (GameApp.isKeyPressed(Input.Keys.SPACE) && player.isOnGround) {
            player.velocityY = 800;
            player.isOnGround = false;
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
        player.x = GameApp.clamp(player.x, 0, getWorldWidth() - PLAYER_SIZE);
        obstacle.playerMovement(oldPosY, oldPosX);
        obstacle2.playerMovement(oldPosY, oldPosX);


        GameApp.clearScreen();

        float drawWidth = bgWidth * bgScale;
        float drawHeight = bgHeight * bgScale;

        float xBg = (getWorldWidth() - drawWidth) / 2;
        float yBg = (getWorldHeight() - drawHeight) / 2;


        GameApp.startSpriteRendering();
        GameApp.drawTexture("EuropeBG", xBg, yBg, drawWidth,  drawHeight );
        GameApp.endSpriteRendering();

        GameApp.startShapeRenderingFilled();
        GameApp.drawRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
        GameApp.drawRect(obstacle2.x, obstacle2.y, obstacle2.width, obstacle2.height);
        GameApp.endShapeRendering();

        player.x = GameApp.clamp(player.x, 0, getWorldWidth() - 100);

        GameApp.startSpriteRendering();
        GameApp.drawAnimation("characterWalk",  player.x, player.y, PLAYER_SIZE, PLAYER_SIZE);

        for (Item item : worldItems) {
            if (!item.collected) {
                GameApp.drawTexture("item", item.x, item.y, ITEM_SIZE, ITEM_SIZE);
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
        GameApp.disposeTexture("EuropeBG");
        GameApp.disposeSpritesheet("characterEurope");
        GameApp.disposeAnimation("characterWalk");

    }

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
}