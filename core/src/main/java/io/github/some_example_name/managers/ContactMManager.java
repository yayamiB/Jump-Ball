package io.github.some_example_name.managers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import io.github.some_example_name.GameSettings;
import io.github.some_example_name.objects.GameObject;

public class ContactMManager implements ContactListener {

    public ContactMManager(World world) {
    }
    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        if (fa.getFilterData().categoryBits == GameSettings.BALL_BIT || fb.getFilterData().categoryBits == GameSettings.BALL_BIT) {
            checkCollision(fa, fb, GameSettings.BRICK_BIT);
        }
    }
    private void checkCollision(Fixture fa, Fixture fb, short targetBit) {
        if (fa.getFilterData().categoryBits == targetBit) {
            ((GameObject) fa.getBody().getUserData()).hit();
        } else if (fb.getFilterData().categoryBits == targetBit) {
            ((GameObject) fb.getBody().getUserData()).hit();
        }
    }
    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
