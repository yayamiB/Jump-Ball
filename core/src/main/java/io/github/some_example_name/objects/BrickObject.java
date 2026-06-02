package io.github.some_example_name.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import io.github.some_example_name.GameResourses;
import io.github.some_example_name.GameSession;
import io.github.some_example_name.GameSettings;
import io.github.some_example_name.MyGdxGame;

public class BrickObject extends GameObject {
    private boolean isAlive;
    private int hp;
    public BrickObject(int x, int y, World world, int maxHp) {
        super(GameResourses.BRICK_IMG_PATH, x, y, GameSettings.BRICK_WIDTH, GameSettings.BRICK_HEIGHT, GameSettings.BRICK_BIT, world);
        this.body.setUserData(this);
        this.body.setType(BodyDef.BodyType.StaticBody);
        this.isAlive = true;
        this.hp = maxHp;
        updateTexture();
        Filter filter = new Filter();
        filter.categoryBits = GameSettings.BRICK_BIT;
        filter.maskBits = GameSettings.BALL_BIT;
        for (Fixture fixture : body.getFixtureList()) {
            fixture.setFilterData(filter);
        }
    }

    @Override
    public void hit() {
        this.hp--;
        if (this.hp <= 0) {
            isAlive = false;
            GameSession.destructionRegistration(10);
        } else {
            updateTexture();
        }
        ((MyGdxGame) Gdx.app.getApplicationListener()).audioManager.playBrickBreak();
    }

    private void updateTexture() {
        if (this.hp == 1) {
            this.texture = new Texture("button_background_short.png");
        } else if (this.hp == 2) {
            this.texture = new Texture("button_background_short_MediumHp.png");
        } else if (this.hp == 3) {
            this.texture = new Texture("button_background_short_MaxHp.png");
        }
    }
    public boolean isAlive() {
        return isAlive;
    }
}
