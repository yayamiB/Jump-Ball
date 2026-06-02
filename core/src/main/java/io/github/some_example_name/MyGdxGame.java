package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.some_example_name.managers.AudioManager;
import io.github.some_example_name.screens.GameScreen;
import io.github.some_example_name.screens.MenuScreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import static io.github.some_example_name.GameSettings.*;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MyGdxGame extends Game {
    public SpriteBatch batch;
    public OrthographicCamera camera;
    public GameScreen gameScreen;
    public World world;
    public BitmapFont largeWhiteFont;
    public BitmapFont commonWhiteFont;
    public BitmapFont commonBlackFont;
    public Vector3 touch;
    float accumulator = 0;
    public MenuScreen menuScreen;
    public AudioManager audioManager;

    @Override
    public void create() {
        Box2D.init();
        largeWhiteFont = FontBuilder.generate(48, Color.WHITE, GameResourses.FONT_PATH);
        commonWhiteFont = FontBuilder.generate(24, Color.WHITE, GameResourses.FONT_PATH);
        commonBlackFont = FontBuilder.generate(24, Color.BLACK, GameResourses.FONT_PATH);
        world = new World(new Vector2(0, 0), true);
        batch = new SpriteBatch();
        touch = new Vector3();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
        gameScreen = new GameScreen(this);
        menuScreen = new MenuScreen(this);
        audioManager = new AudioManager();
        setScreen(menuScreen);
    }


    @Override
    public void dispose() {
        batch.dispose();
        world.dispose();
        largeWhiteFont.dispose();
        commonWhiteFont.dispose();
        commonBlackFont.dispose();
        audioManager.dispose();
    }
    public void stepWorld() {
        float delta = Gdx.graphics.getDeltaTime();
        accumulator += Math.min(delta, 0.25f);
        if (accumulator >= STEP_TIME) {
            accumulator -= STEP_TIME;
            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_INTERATIONS);
        }
    }
}
