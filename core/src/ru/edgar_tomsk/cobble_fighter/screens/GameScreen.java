package ru.edgar_tomsk.cobble_fighter.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
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

    Texture backgroundTexture;
    Texture cobbleTexture;
    Texture gloveTexture;
    Texture pauseButtonTexture;

    ArrayList<CobbleObject> cobbleArray;

    Music backgroundMusic;
    Sound hitSound;

    Rectangle pauseButtonRect;

    boolean isAlive = true;
    public GameScreen(CobbleFighter CobbleFighter) {
        this.CobbleFighter = CobbleFighter;

        Array<Body> bodies = new Array<>();
        CobbleFighter.world.getBodies(bodies);
        for (Body body : bodies) {
            CobbleFighter.world.destroyBody(body);
        }

        gameSession = new GameSession();
        cobbleArray = new ArrayList<>();

        backgroundTexture = new Texture(GameResources.BACKGROUND_IMG_PATH);
        cobbleTexture = new Texture(GameResources.COBBLE_IMG_PATH);
        gloveTexture = new Texture(GameResources.GLOVE_IMG_PATH);
        pauseButtonTexture = new Texture(GameResources.BUTTON_PAUSE_IMG_PATH);

        int buttonSize = 80;
        pauseButtonRect = new Rectangle(
                GameSettings.SCREEN_WIDTH - buttonSize - 20,
                GameSettings.SCREEN_HEIGHT - buttonSize - 20,
                buttonSize,
                buttonSize
        );

        gloveObject = new GloveObject(
                GameSettings.SCREEN_WIDTH / 2, 150,
                GameSettings.GLOVE_WIDTH, GameSettings.GLOVE_HEIGHT,
                GameResources.GLOVE_IMG_PATH,
                CobbleFighter.world
        );

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(GameResources.BACKGROUND_MUSIC_PATH));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f);
        backgroundMusic.play();

        hitSound = Gdx.audio.newSound(Gdx.files.internal(GameResources.HIT_SOUND_PATH));

        CobbleFighter.world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Object dataA = contact.getFixtureA().getBody().getUserData();
                Object dataB = contact.getFixtureB().getBody().getUserData();

                if ((isGlove(dataA) && isCobble(dataB)) || (isGlove(dataB) && isCobble(dataA))) {
                    hitSound.play();
                    isAlive = false;
                }
            }

            private boolean isGlove(Object data) {
                return "glove".equals(data);
            }

            private boolean isCobble(Object data) {
                return "cobble".equals(data);
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });
    }

    @Override
    public void show() {
        gameSession.startGame();
    }

    @Override
    public void render(float delta) {
        handleInput();

        if (gameSession.state == GameSession.GameState.PAUSED) {
            draw();
            return;
        }

        if (!isAlive) {
            CobbleFighter.setScreen(new RestartScreen(CobbleFighter));
            dispose();
            return;
        }

        CobbleFighter.stepWorld();

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

    @Override
    public void dispose() {
        if (cobbleTexture != null) cobbleTexture.dispose();
        if (gloveTexture != null) gloveTexture.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (pauseButtonTexture != null) pauseButtonTexture.dispose();

        if (backgroundMusic != null) backgroundMusic.dispose();
        if (hitSound != null) hitSound.dispose();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            CobbleFighter.touch = CobbleFighter.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (pauseButtonRect.contains(CobbleFighter.touch.x, CobbleFighter.touch.y)) {
                if (gameSession.state == GameSession.GameState.PAUSED) {
                    gameSession.state = GameSession.GameState.PLAYING;
                    backgroundMusic.play();
                } else {
                    gameSession.state = GameSession.GameState.PAUSED;
                    backgroundMusic.pause();
                }
                return;
            }
        }

        if (gameSession.state == GameSession.GameState.PLAYING && Gdx.input.isTouched()) {
            CobbleFighter.touch = CobbleFighter.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            if (!pauseButtonRect.contains(CobbleFighter.touch.x, CobbleFighter.touch.y)) {
                gloveObject.move(CobbleFighter.touch);
            }
        }
    }

    private void draw() {
        CobbleFighter.camera.update();
        CobbleFighter.batch.setProjectionMatrix(CobbleFighter.camera.combined);
        ScreenUtils.clear(Color.CLEAR);

        CobbleFighter.batch.begin();

        CobbleFighter.batch.draw(backgroundTexture, 0, 0, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);

        for (CobbleObject cobble : cobbleArray) cobble.draw(CobbleFighter.batch);
        gloveObject.draw(CobbleFighter.batch);

        CobbleFighter.font.draw(CobbleFighter.batch, "Score: " + gameSession.score, 50, GameSettings.SCREEN_HEIGHT - 50);

        CobbleFighter.batch.draw(pauseButtonTexture, pauseButtonRect.x, pauseButtonRect.y, pauseButtonRect.width, pauseButtonRect.height);

        if (gameSession.state == GameSession.GameState.PAUSED) {
            CobbleFighter.font.draw(CobbleFighter.batch, "PAUSED", GameSettings.SCREEN_WIDTH / 2f - 100, GameSettings.SCREEN_HEIGHT / 2f);
        }

        CobbleFighter.batch.end();
    }

    private void updateCobble() {
        for (int i = 0; i < cobbleArray.size(); i++) {
            if (!cobbleArray.get(i).isInFrame()) {
                CobbleFighter.world.destroyBody(cobbleArray.get(i).body);
                cobbleArray.remove(i--);

                gameSession.score++;
            }
        }
    }
}
