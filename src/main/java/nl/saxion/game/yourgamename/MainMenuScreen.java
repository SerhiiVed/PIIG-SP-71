package nl.saxion.game.yourgamename;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import nl.saxion.gameapp.GameApp;
import nl.saxion.gameapp.screens.ScalableGameScreen;

public class MainMenuScreen extends ScalableGameScreen {

    BitmapFont fontTitle;
    BitmapFont fontMenu;
    SpriteBatch myBatch;

    public MainMenuScreen() {
        super(1280, 720);
    }

    @Override
    public void show() {
        // Initialization (Must be done in show())

        // Title Font Setup
        fontTitle = new BitmapFont();
        fontTitle.getData().setScale(3.0f);
        fontTitle.setColor(Color.RED);

        // Menu Font Setup
        fontMenu = new BitmapFont();
        fontMenu.getData().setScale(1.5f);
        fontMenu.setColor(Color.WHITE);

        myBatch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        GameApp.clearScreen();

        myBatch.begin();

        // DRAWING LOGIC (Using separate fonts)
        fontTitle.draw(myBatch, "DEUX EX MACHINA", 200, 600); // Increased Y for visibility

        fontMenu.setColor(Color.YELLOW);
        fontMenu.draw(myBatch, "Start Game: Europe \n (PRESS LEVEL)", 200, 500); // Adjusted Y

        fontMenu.setColor(Color.GRAY);
        fontMenu.draw(myBatch, "1. VERY HARD", 200, 400); // Adjusted Y
        fontMenu.draw(myBatch, "2. HARD", 200, 350);
        fontMenu.draw(myBatch, "3. NORMAL", 200, 300);
        fontMenu.draw(myBatch, "4. EASY", 200, 250);

        myBatch.end();

        // INPUT LOGIC (Using else if for safe switching)
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1) || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            // Level 1 / ENTER: SideViewScreen (representative level)
            GameApp.switchScreen("SideViewScreen");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            // Level 2: YourGameScreen
            GameApp.switchScreen("YourGameScreen");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            // Level 3: Placeholder for future implementation
            // GameApp.switchScreen("Level3Screen");
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
            // Level 4: Placeholder for future implementation
            // GameApp.switchScreen("Level4Screen");
        }
    } // End of render method

    @Override
    public void resize(int width, int height) {}

    @Override
    public void hide() {
        // Required method for Screen interface
    } // End of hide method

    @Override
    public void dispose() {
        // Clean up both font objects
        if (fontTitle != null) fontTitle.dispose();
        if (fontMenu != null) fontMenu.dispose();
        if (myBatch != null) myBatch.dispose();
    } // End of dispose method

} // End of MainMenuScreen class