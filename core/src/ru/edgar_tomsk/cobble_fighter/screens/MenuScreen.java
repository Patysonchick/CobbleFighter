package ru.edgar_tomsk.cobble_fighter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.edgar_tomsk.cobble_fighter.CobbleFighter;
import ru.edgar_tomsk.cobble_fighter.GameResources;
import ru.edgar_tomsk.cobble_fighter.GameSettings;

public class MenuScreen extends ScreenAdapter {

    CobbleFighter game;
    Texture playButtonTexture;
    Texture backgroundTexture;

    GlyphLayout layout = new GlyphLayout();

    int buttonWidth = 300;
    int buttonHeight = 150;
    int buttonX;
    int buttonY;

    public MenuScreen(CobbleFighter game) {
        this.game = game;
        playButtonTexture = new Texture(GameResources.BUTTON_BG_IMG_PATH);
        backgroundTexture = new Texture(GameResources.BACKGROUND_IMG_PATH);

        buttonX = (GameSettings.SCREEN_WIDTH - buttonWidth) / 2;
        buttonY = (GameSettings.SCREEN_HEIGHT - buttonHeight) / 2;
    }

    @Override
    public void render(float delta) {
        handleInput();

        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        game.batch.begin();

        game.batch.draw(backgroundTexture, 0, 0, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
        game.batch.draw(playButtonTexture, buttonX, buttonY, buttonWidth, buttonHeight);

        String text = "PLAY";
        layout.setText(game.font, text);
        float textX = buttonX + (buttonWidth - layout.width) / 2;
        float textY = buttonY + (buttonHeight + layout.height) / 2;
        game.font.draw(game.batch, text, textX, textY);

        game.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            game.touch = game.camera.unproject(game.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (game.touch.x > buttonX && game.touch.x < buttonX + buttonWidth &&
                    game.touch.y > buttonY && game.touch.y < buttonY + buttonHeight) {

                game.setScreen(new GameScreen(game));
                dispose();
            }
        }
    }

    @Override
    public void dispose() {
        playButtonTexture.dispose();
        backgroundTexture.dispose();
    }
}