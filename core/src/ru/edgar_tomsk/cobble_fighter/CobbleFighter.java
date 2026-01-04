package ru.edgar_tomsk.cobble_fighter;

import static ru.edgar_tomsk.cobble_fighter.GameSettings.POSITION_ITERATIONS;
import static ru.edgar_tomsk.cobble_fighter.GameSettings.STEP_TIME;
import static ru.edgar_tomsk.cobble_fighter.GameSettings.VELOCITY_ITERATIONS;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;

public class CobbleFighter extends Game {

    public World world;

    public Vector3 touch;
    public SpriteBatch batch;
    public OrthographicCamera camera;
    public BitmapFont font;

    float accumulator = 0;

    @Override
    public void create() {
        Box2D.init();
        world = new World(new Vector2(0, 0), true);

        touch = new Vector3();
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(5f);

        setScreen(new ru.edgar_tomsk.cobble_fighter.screens.MenuScreen(this));
    }

    @Override
    public void dispose() {
        batch.dispose();
        if (world != null) world.dispose();
        if (font != null) font.dispose();
    }

    public void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();
        accumulator += Math.min(delta, 0.25f);

        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;
            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
    }
}
