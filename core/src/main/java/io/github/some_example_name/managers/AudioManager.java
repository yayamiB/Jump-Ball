package io.github.some_example_name.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {
    private Music backgroundMusic;
    private Sound hitBrickSound;
    private Sound loseLifeSound;
    private Sound levelCompleteSound;

    public AudioManager() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("BackgroundMusic.mp3"));
        hitBrickSound = Gdx.audio.newSound(Gdx.files.internal("BreakBrick.mp3"));
        loseLifeSound = Gdx.audio.newSound(Gdx.files.internal("damage.mp3"));
        levelCompleteSound = Gdx.audio.newSound(Gdx.files.internal("levelfinish.mp3"));
        backgroundMusic.setVolume(0.1f);
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }
    public void playBrickBreak() {
        hitBrickSound.play(0.5f);
    }

    public void playLifeLost() {
        loseLifeSound.play(0.5f);
    }

    public void playLevelComplete() {
        levelCompleteSound.play(0.5f);
    }
    public void dispose() {
        backgroundMusic.dispose();
        hitBrickSound.dispose();
        loseLifeSound.dispose();
        levelCompleteSound.dispose();
    }
}
