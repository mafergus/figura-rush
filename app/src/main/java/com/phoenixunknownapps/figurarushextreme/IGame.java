package com.phoenixunknownapps.figurarushextreme;

public interface IGame {
	public interface GameCallback {
		public void onGameEnd(long timeMs, int score);
		public void onGameWon(long timeMs, int score);
	}

}
