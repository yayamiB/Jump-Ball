package io.github.some_example_name.objects;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import io.github.some_example_name.GameResourses;
import io.github.some_example_name.GameSettings;

public class PaddleObject extends GameObject {
    public PaddleObject(int x, int y, World world) {
        super(GameResourses.PADDLE_IMG_PATH, x, y, GameSettings.PADDLE_WIDTH, GameSettings.PADDLE_HEIGHT, GameSettings.PADDLE_BIT, world);
        this.body.setUserData(this);
        this.body.setType(BodyDef.BodyType.KinematicBody);
    }
    public void move(float touchX) {
        float targetX = touchX * GameSettings.SCALE;
        float minX = 50f * GameSettings.SCALE;
        float maxX = 670f * GameSettings.SCALE;
        if (targetX < minX) targetX = minX;
        if (targetX > maxX) targetX = maxX;
        body.setTransform(targetX, body.getPosition().y, 0);
    }
}
