package ru.edgar_tomsk.cobble_fighter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;

import ru.edgar_tomsk.cobble_fighter.CobbleFighter;
import ru.edgar_tomsk.cobble_fighter.GameResources;
import ru.edgar_tomsk.cobble_fighter.GameSession;
import ru.edgar_tomsk.cobble_fighter.GameSettings;
import ru.edgar_tomsk.cobble_fighter.objects.CobbleObject;
import ru.edgar_tomsk.cobble_fighter.objects.GloveObject;

public class GameScreen extends ScreenAdapter {

    CobbleFighter CobbleFighter;
    GameSession gameSession;
    GloveObject gloveObject;

    ArrayList<CobbleObject> cobbleArray;

    public GameScreen(CobbleFighter CobbleFighter) {
        this.CobbleFighter = CobbleFighter;
        gameSession = new GameSession();

        cobbleArray = new ArrayList<>();

        gloveObject = new GloveObject(
                GameSettings.SCREEN_WIDTH / 2, 150,
                GameSettings.GLOVE_WIDTH, GameSettings.GLOVE_HEIGHT,
                GameResources.GLOVE_IMG_PATH,
                CobbleFighter.world
        );

    }

    @Override
    public void show() {
        gameSession.startGame();
    }

    @Override
    public void render(float delta) {

        CobbleFighter.stepWorld();
        handleInput();

        if (gameSession.shouldSpawnCobble()) {
            CobbleObject cobbleObject = new CobbleObject(
                    GameSettings.COBBLE_WIDTH, GameSettings.COBBLE_HEIGHT,
                    GameResources.COBBLE_IMG_PATH,
                    CobbleFighter.world
            );
            cobbleArray.add(cobbleObject);
        }

        updateCobble();

        draw();
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            CobbleFighter.touch = CobbleFighter.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            gloveObject.move(CobbleFighter.touch);
        }
    }

    private void draw() {

        CobbleFighter.camera.update();
        CobbleFighter.batch.setProjectionMatrix(CobbleFighter.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        CobbleFighter.batch.begin();
        for (CobbleObject cobble : cobbleArray) cobble.draw(CobbleFighter.batch);
        gloveObject.draw(CobbleFighter.batch);
        CobbleFighter.batch.end();

    }

    private void updateCobble() {
        for (int i = 0; i < cobbleArray.size(); i++) {
            if (!cobbleArray.get(i).isInFrame()) {
                CobbleFighter.world.destroyBody(cobbleArray.get(i).body);
                cobbleArray.remove(i--);
            }
        }
    }
}
