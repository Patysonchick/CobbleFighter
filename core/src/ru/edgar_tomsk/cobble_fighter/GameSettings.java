package ru.edgar_tomsk.cobble_fighter;

public class GameSettings {

    // Настройки устройства

    public static final int SCREEN_WIDTH = 720;
    public static final int SCREEN_HEIGHT = 1280;

    // Физические данные

    public static final float STEP_TIME = 1f / 60f;
    public static final int VELOCITY_ITERATIONS = 6;
    public static final int POSITION_ITERATIONS = 6;
    public static final float SCALE = 0.05f;
    public static final int GLOVE_WIDTH = 100;
    public static final int GLOVE_HEIGHT = 200;
    public static final int COBBLE_WIDTH = 150;

    // Габариты объекта
    public static final int COBBLE_HEIGHT = 200;
    public static float GLOVE_FORCE_RATIO = 10;
    public static float COBBLE_VELOCITY = 10;
    public static long STARTING_COBBLE_APPEARANCE_COOL_DOWN = 5000;

}
