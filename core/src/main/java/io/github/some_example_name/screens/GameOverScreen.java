package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import io.github.some_example_name.MyGdxGame;
import io.github.some_example_name.components.ButtonView;
import io.github.some_example_name.GameResourses;

public class GameOverScreen extends ScreenAdapter {
    private final MyGdxGame game;
    private final ButtonView restartButton;
    private final ButtonView menuButton;
    public GameOverScreen(MyGdxGame game) {
        this.game = game;
        this.restartButton = new ButtonView(210, 700, 300, 100, game.commonBlackFont, GameResourses.BUTTON_LONG_BG_IMG_PATH, "RESTART");
        this.menuButton = new ButtonView(210, 500, 300, 100, game.commonBlackFont, GameResourses.BUTTON_LONG_BG_IMG_PATH, "MENU");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        restartButton.draw(game.batch);
        menuButton.draw(game.batch);
        game.batch.end();
        if (Gdx.input.justTouched()) {
            Vector3 touch = game.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (restartButton.isHit(touch.x, touch.y)) {
                game.setScreen(new GameScreen(game));
            }
            if (menuButton.isHit(touch.x, touch.y)) {
                game.setScreen(new MenuScreen(game));
            }
        }
    }
}
