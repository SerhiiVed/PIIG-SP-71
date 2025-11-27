package nl.saxion.game.yourgamename;

import com.badlogic.gdx.Input;
import nl.saxion.gameapp.GameApp;
import nl.saxion.gameapp.screens.ScalableGameScreen;

public class TopDownScreen extends ScalableGameScreen {
    String randomBoxColor = "violet-500";
    String randomBackgroundColor = "black";
    Player PlayerCharacter = new Player();

    public TopDownScreen() {
        super(1280, 720);
    }

    @Override
    public void show() {
//        randomBoxColor = getRandomColor();
        PlayerCharacter.TopDownWidth = 40;
        PlayerCharacter.TopDownHeight = 40;
        // Calculate where the box would be (we draw it in the center of the world)
        PlayerCharacter.x = getWorldWidth() / 2 - PlayerCharacter.TopDownWidth / 2;
        PlayerCharacter.y = getWorldHeight() / 2 - PlayerCharacter.TopDownHeight / 2;
    }

    @Override
    public void render(float delta) {
        super.render(delta);


        float speed = 500;

        if (GameApp.isKeyPressed(Input.Keys.W)) {
            if (!((PlayerCharacter.y + PlayerCharacter.TopDownHeight) >= getWorldHeight())) {
                PlayerCharacter.y += speed * delta;
            }
        }
        if (GameApp.isKeyPressed(Input.Keys.S)) {
            if (!(PlayerCharacter.y <= 0)){
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



        // Draw elements
        GameApp.clearScreen(randomBackgroundColor);
        GameApp.startShapeRenderingFilled();
        GameApp.drawRect(PlayerCharacter.x, PlayerCharacter.y, PlayerCharacter.TopDownWidth, PlayerCharacter.TopDownHeight, randomBoxColor);
        GameApp.endShapeRendering();

    }

    private String getRandomColor() {
        int randomIndex = (int)GameApp.random(0, GameApp.getAllColors().length-1);
        return GameApp.getAllColors()[randomIndex];
    }

    @Override
    public void hide() {

    }
}
