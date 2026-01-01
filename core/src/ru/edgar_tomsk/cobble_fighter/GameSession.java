package ru.edgar_tomsk.cobble_fighter;

import com.badlogic.gdx.utils.TimeUtils;

public class GameSession {

    long nextCobbleSpawnTime;
    long sessionStartTime;

    public GameSession() {
    }

    public void startGame() {
        sessionStartTime = TimeUtils.millis();
        nextCobbleSpawnTime = sessionStartTime + (long) (GameSettings.STARTING_COBBLE_APPEARANCE_COOL_DOWN
                * getCobblePeriodCoolDown());
    }

    public boolean shouldSpawnCobble() {
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
}
