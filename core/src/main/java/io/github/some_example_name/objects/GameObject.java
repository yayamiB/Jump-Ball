package io.github.some_example_name.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import static io.github.some_example_name.GameSettings.SCALE;

public class GameObject {
    public short cBits;
    public int width;
    public int height;
    public Body body;
    Texture texture;

    GameObject(String texturePath, int x, int y, int width, int height, short cBits, World world) {
        this.width = width;
        this.height = height;
        this.cBits = cBits;
        this.texture = new Texture(texturePath);
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x * SCALE, y * SCALE);
        this.body = world.createBody(def);
        createFixture();
    }
    protected void createFixture() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width * SCALE / 2f, height * SCALE / 2f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 1.0f;
        fixtureDef.filter.categoryBits = cBits;
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture,
            getX() - (width / 2f),
            getY() - (height / 2f),
            width,
            height);
    }

    public void hit() {
    }

    public int getX() {
        return (int) (body.getPosition().x / SCALE);
    }

    public int getY() {
        return (int) (body.getPosition().y / SCALE);
    }

    public void setX(int x) {
        body.setTransform(x * SCALE, body.getPosition().y, 0);
    }

    public void setY(int y) {
        body.setTransform(body.getPosition().x, y * SCALE, 0);
    }
}
