package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import io.github.some_example_name.MyGdxGame;
import io.github.some_example_name.GameResourses;
import io.github.some_example_name.components.ButtonView;
import io.github.some_example_name.components.MovingBackgroundView;
import io.github.some_example_name.components.TextView;

public class MenuScreen extends ScreenAdapter {
    private final MyGdxGame myGdxGame;
    private MovingBackgroundView background;
    private final TextView titleText;
    private final ButtonView startButton;
    private final ButtonView exitButton;

    public MenuScreen(MyGdxGame game) {
        this.myGdxGame = game;
        titleText = new TextView(game.largeWhiteFont, 210, 1000, "JUMP BALL");
        startButton = new ButtonView(
            210, 600,
            300, 100,
            game.commonBlackFont,
            GameResourses.BUTTON_LONG_BG_IMG_PATH,
            "START GAME"
        );
        exitButton = new ButtonView(
            210, 450,
            300, 100,
            game.commonBlackFont,
            GameResourses.BUTTON_LONG_BG_IMG_PATH,
            "EXIT"
        );
    }

    @Override
    public void show() {
        background = new MovingBackgroundView(GameResourses.MENU_BACKGROUND_IMG_PATH);
    }

    @Override
    public void render(float delta) {
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        handleInput();
        myGdxGame.batch.begin();
        background.draw(myGdxGame.batch);
        titleText.draw(myGdxGame.batch);
        startButton.draw(myGdxGame.batch);
        exitButton.draw(myGdxGame.batch);
        myGdxGame.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (startButton.isHit(touch.x, touch.y)) {
                myGdxGame.setScreen(new GameScreen(myGdxGame));
                dispose();

            }
            if (exitButton.isHit(touch.x, touch.y)) {
                Gdx.app.exit();
            }
        }
    }

    @Override
    public void dispose() {
        if (background != null) {
            background.dispose();
        }
    }
}
