package ru.edgar_tomsk.cobble_fighter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.edgar_tomsk.cobble_fighter.CobbleFighter;
import ru.edgar_tomsk.cobble_fighter.GameResources;
import ru.edgar_tomsk.cobble_fighter.GameSettings;

public class RestartScreen extends ScreenAdapter {
    CobbleFighter game;
    Texture backgroundTexture;

    public RestartScreen(CobbleFighter game) {
        this.game = game;
        backgroundTexture = new Texture(GameResources.BACKGROUND_IMG_PATH);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }

        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        game.batch.begin();

        game.batch.draw(backgroundTexture, 0, 0, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);

        game.font.setColor(Color.RED);
        game.font.draw(game.batch, "GAME OVER", 200, 700);

        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, "Tap to Restart", 180, 600);

        game.batch.end();
    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
    }
}