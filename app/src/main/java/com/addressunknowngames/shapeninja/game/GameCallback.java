package com.addressunknowngames.shapeninja.game;

public interface GameCallback {
	void onGameEnd(long timeMs, int score);
	void onGameWon(long timeMs, int score);
}
