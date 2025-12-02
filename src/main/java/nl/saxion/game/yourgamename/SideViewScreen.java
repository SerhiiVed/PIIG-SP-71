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
    float velocityY = 0;
    boolean isOnGround;
    public SideViewScreen() {
        super(1280, 720);
    }
    public static final int PLAYER_SPEED = 600;
    public static final int ITEM_SIZE= 100;
    public static final int PLAYER_SIZE= 100;

    @Override
    public void show() {
        GameApp.addTexture("chatGpt", "textures/ChatGPT.png");
        GameApp.addTexture("item", "textures/item.PNG");
        GameApp.addFont("basicFont", "fonts/basic.ttf", 60);

        player = new Player();
        player.x = GameApp.getWorldWidth() / 2;
        player.y = 0;

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
        super.render(delta);
        GameApp.clearScreen();

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


        for (Item item : worldItems) {
            if (!item.collected && GameApp.rectOverlap( player.x, player.y, PLAYER_SIZE, PLAYER_SIZE, item.x, item.y, ITEM_SIZE, ITEM_SIZE)) {

                collectItem(item);
            }
        }
        player.x = GameApp.clamp(player.x, 0, getWorldWidth() - PLAYER_SIZE);

        GameApp.startSpriteRendering();

        GameApp.drawTexture("chatGpt", player.x, player.y, PLAYER_SIZE, PLAYER_SIZE);
        for (Item item : worldItems) {
            if (!item.collected) {
                GameApp.drawTexture("item", item.x, item.y, ITEM_SIZE, ITEM_SIZE);
            }
        }
        int y = 600;
        GameApp.drawText("basicFont", "Inventory", 50, y, Color.WHITE);
        y -= 50;

        for (Item i : inventory) {
            GameApp.drawText("basicFont", i.name + " x" + i.amount, 50, y, Color.WHITE);
            y += 22;
        }

        GameApp.endSpriteRendering();

    }

    @Override
    public void hide() {
        GameApp.disposeTexture("chatGpt");
        GameApp.disposeTexture("item");
        GameApp.disposeTexture("basicFont");
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