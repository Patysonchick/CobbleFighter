package ru.edgar_tomsk.cobble_fighter;

import com.badlogic.gdx.utils.TimeUtils;

public class GameSession {
    public int score;

    long nextCobbleSpawnTime;
    long sessionStartTime;
    public GameState state;
    long startTime;

    public GameSession() {
    }

    public void startGame() {
        sessionStartTime = TimeUtils.millis();
        nextCobbleSpawnTime = sessionStartTime + (long) (GameSettings.STARTING_COBBLE_APPEARANCE_COOL_DOWN
                * getCobblePeriodCoolDown());

        state = GameState.PLAYING;
        startTime = TimeUtils.millis();

        score = 0;
    }

    public boolean shouldSpawnCobble() {
        if (state != GameState.PLAYING) return false;

        if (nextCobbleSpawnTime <= TimeUtils.millis()) {
            nextCobbleSpawnTime = TimeUtils.millis() + (long) (GameSettings.STARTING_COBBLE_APPEARANCE_COOL_DOWN
                    * getCobblePeriodCoolDown());
            return true;
        }
        return false;
    }

    private float getCobblePeriodCoolDown() {
        return (float) Math.exp(-0.001 * (TimeUtils.millis() - sessionStartTime + 1) / 1000);
    }

    public enum GameState {
        PLAYING, PAUSED
    }
}
