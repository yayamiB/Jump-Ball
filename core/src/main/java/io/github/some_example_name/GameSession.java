package io.github.some_example_name;

public class GameSession {
    private static int score;

    public GameSession() {
    }
    public static int getScore() {
        return score;
    }

    public static void resetScore() {
        score = 0;
    }
    public static void destructionRegistration(int points) {
        score += points;
    }
}
