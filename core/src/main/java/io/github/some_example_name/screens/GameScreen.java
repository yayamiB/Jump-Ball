package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import io.github.some_example_name.*;
import io.github.some_example_name.components.*;
import io.github.some_example_name.managers.ContactMManager;
import io.github.some_example_name.objects.*;

import java.util.ArrayList;
import java.util.Iterator;

import static io.github.some_example_name.GameSession.resetScore;
import static io.github.some_example_name.GameSettings.SCALE;
import static io.github.some_example_name.GameSettings.SCREEN_HEIGHT;

public class GameScreen extends ScreenAdapter {
    private final MyGdxGame myGdxGame;
    private final World world;
    private final ContactMManager contactMManager;
    private PaddleObject paddle;
    private BallObject ball;
    private final ArrayList<BrickObject> bricks = new ArrayList<>();
    private final MovingBackgroundView background;
    private final TextView scoreText;
    private final LiveView livesView;
    private final ButtonView pauseButton;
    private final ButtonView menuButton;
    private int lives = 3;
    private boolean isPaused = false;
    private boolean isLevelFinished = false;

    public GameScreen(MyGdxGame game) {
        this.myGdxGame = game;
        this.world = new World(new Vector2(0, 0), true);
        createWalls();
        this.contactMManager = new ContactMManager(world);
        this.world.setContactListener(contactMManager);
        background = new MovingBackgroundView(GameResourses.BACKGROUND_IMG_PATH);
        scoreText = new TextView(game.commonWhiteFont, 40, 1200, "SCORE: 0");
        livesView = new LiveView(500, 1200);
        pauseButton = new ButtonView(650, 1200, 50, 50, GameResourses.PAUSE_IMG_PATH);
        menuButton = new ButtonView(260, 500, 200, 60, GameResourses.BUTTON_LONG_BG_IMG_PATH);
        initGame();
    }

    private void createWalls() {
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.StaticBody;
        bd.position.set(0, 0);
        Body wallBody = world.createBody(bd);
        EdgeShape edgeShape = new EdgeShape();
        FixtureDef fd = new FixtureDef();
        fd.shape = edgeShape;
        fd.friction = 0f;
        fd.restitution = 1f;
        fd.filter.categoryBits = 1;
        fd.filter.maskBits = -1;
        float w = GameSettings.SCREEN_WIDTH * SCALE;
        float h = SCREEN_HEIGHT * SCALE;
        edgeShape.set(0, 0, 0, h);
        wallBody.createFixture(fd);
        edgeShape.set(w, 0, w, h);
        wallBody.createFixture(fd);
        edgeShape.set(0, h, w, h);
        wallBody.createFixture(fd);
        edgeShape.dispose();
    }

    private void initGame() {
        paddle = new PaddleObject(360, 100, world);
        ball = new BallObject(360, 300, world);
        lives = 3;
        livesView.setLeftLives(lives);
        resetScore();
        generateNewLevel();
        ball.push(5, 5);
    }

    @Override
    public void render(float delta) {
        handleInput();
        if (!isPaused) {
            world.step(1/60f, 6, 2);
            cleanDeadBricks();
            updateGame();
            if (isLevelFinished) {
                generateNewLevel();
                resetBall();
                myGdxGame.audioManager.playLevelComplete();
                isLevelFinished = false;
            }
            if (ball.getY() < 0) {
                lives--;
                myGdxGame.audioManager.playLifeLost();
                livesView.setLeftLives(lives);
                if (lives <= 0) {
                    gameOver();
                }
                resetBall();
            }
        }
        draw();
    }

    private void updateGame() {
        boolean hasAliveBricks = false;
        for (BrickObject b : bricks) {
            if (b.isAlive()) {
                hasAliveBricks = true;
                break;
            }
        }
        if (!hasAliveBricks) {
            isLevelFinished = true;
        }
    }

    private void generateNewLevel() {
        for (BrickObject b : bricks) {
            if (b != null && b.body != null) {
                world.destroyBody(b.body);
                b.body = null;
            }
        }
        bricks.clear();
        int levelType = MathUtils.random(0, 5);
        int rows = 5;
        int cols = 6;
        int startX = 80;
        int startY = 900;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                boolean spawn = false;
                switch (levelType) {
                    case 0:
                        if (i == 0 && (j == 2 || j == 3)) spawn = true;
                        else if (i == 1 && j >= 1 && j <= 4) spawn = true;
                        else if (i >= 2) spawn = true;
                        break;
                    case 1:
                        if (j == 0 ||  j == 1 ||  j == 4 || j == 5) spawn = true;
                        break;
                    case 2:
                        if ((i + j) % 2 == 0) spawn = true;
                        break;
                    case 3:
                        if (i == 0 ||  i == rows - 1 || j == 0 || j == cols - 1) spawn = true;
                        break;
                    case 4:
                        if (i == 2 || j == 2 || j == 3) spawn = true;
                        break;
                    case 5:
                        if ((i + j) % 3 == 0) spawn = true;
                        break;
                }
                if (spawn) {
                    int x = startX + j * 110;
                    int y = startY + i * 60;
                    int randomHp = MathUtils.random(1, 3);
                    BrickObject newBrick = new BrickObject(x, y, world, randomHp);
                    if (newBrick.body != null) {
                        newBrick.body.setUserData(newBrick);
                        Filter filter = new Filter();
                        filter.categoryBits = GameSettings.BRICK_BIT;
                        filter.maskBits = GameSettings.BALL_BIT;
                        for (Fixture fixture : newBrick.body.getFixtureList()) {
                            fixture.setFilterData(filter);
                            fixture.setSensor(false);
                        }
                    }
                    bricks.add(newBrick);
                }
            }
        }
    }
    private void draw() {
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        myGdxGame.batch.begin();
        background.draw(myGdxGame.batch);
        scoreText.setText("SCORE: " + GameSession.getScore());
        scoreText.draw(myGdxGame.batch);
        livesView.draw(myGdxGame.batch);
        pauseButton.draw(myGdxGame.batch);
        paddle.draw(myGdxGame.batch);
        ball.draw(myGdxGame.batch);
        for (BrickObject b : bricks) {
            if (b.isAlive()) b.draw(myGdxGame.batch);
        }
        if (isPaused) {
            myGdxGame.largeWhiteFont.draw(myGdxGame.batch, "PAUSE", 270, 750);
            menuButton.draw(myGdxGame.batch);
            myGdxGame.commonBlackFont.draw(myGdxGame.batch, "MENU", 315, 538);
        }
        myGdxGame.batch.end();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            Vector3 touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (pauseButton.isHit(touch.x, touch.y)) {
                isPaused = !isPaused;
                return;

            }
            if (isPaused && menuButton.isHit(touch.x, touch.y)) {isPaused = false;
                myGdxGame.setScreen(myGdxGame.menuScreen);
                return;

            }
        }
        if (Gdx.input.isTouched()) {
            Vector3 touch = myGdxGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            paddle.move(touch.x);

        }
    }

    private void cleanDeadBricks() {
        Iterator<BrickObject> iterator = bricks.iterator();
        while (iterator.hasNext()) {
            BrickObject b = iterator.next();
            if (!b.isAlive()) {
                if (b.body != null) {
                    world.destroyBody(b.body);
                    b.body = null;
                }
                iterator.remove();
            }
        }
    }

    private void gameOver() {
        myGdxGame.setScreen(new GameOverScreen(myGdxGame));
    }

    private void resetBall() {
        ball.setY(300);
        ball.setX(360);
        if (ball.body != null) {
            ball.body.setLinearVelocity(0, 0);
        }
        ball.push(5, 5);
    }
}

