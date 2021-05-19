package com.intelligentcake.ctf.game;

public enum GameState {

    IN_LOBBY(true),
    IN_GAME(false),
    POST_GAME(false),
    RESETTING(false);

    private final boolean canJoin;
    private GameState currentState;

    GameState(boolean canJoin) {
        this.canJoin = canJoin;
    }

    public boolean canJoin() {
        return  canJoin;
    }

    public void setState(GameState state) {
        currentState = state;
    }

    public boolean isState(GameState state) {
        return currentState == state;
    }

    public GameState getCurrentState() {
        return currentState;
    }
}
