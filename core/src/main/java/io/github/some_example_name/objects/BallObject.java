package io.github.some_example_name.objects;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import io.github.some_example_name.GameResourses;
import io.github.some_example_name.GameSettings;

public class BallObject extends GameObject {
    public BallObject(int x, int y, World world) {
        super(GameResourses.BALL_IMG_PATH, x, y, GameSettings.BALL_RADIUS * 2, GameSettings.BALL_RADIUS * 2, GameSettings.BALL_BIT, world);
        this.body.setUserData(this);
        this.body.setType(BodyDef.BodyType.DynamicBody);
    }
    @Override
    protected void createFixture() {
        CircleShape circle = new CircleShape();
        circle.setRadius(GameSettings.BALL_RADIUS * GameSettings.SCALE);
        FixtureDef fd = new FixtureDef();
        fd.shape = circle;
        fd.density = 0.05f;
        fd.restitution = 1.0f;
        fd.friction = 0f;
        fd.filter.categoryBits = GameSettings.BALL_BIT;
        this.body.createFixture(fd);
        circle.dispose();
    }
    public void push(float vx, float vy) {
        body.setLinearVelocity(vx, vy);
    }
}
