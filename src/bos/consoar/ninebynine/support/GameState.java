package bos.consoar.ninebynine.support;

import java.io.Serializable;

import bos.consoar.ninebynine.support.entity.Block;

public class GameState implements Serializable{
	private static final long serialVersionUID = 1L;
	public int width = Common.width, height =  Common.height;
	public Block[] board;
	public int comboMult;
	public int linesCleared;
	public Block[] nextBlocks;
	public int numMoveAnywhere;
	public int numUndos;
	public int numDeletes;
	public GameState previousState;
	public int score;
	public int leftBlocks;

	public GameState() {
		this.board = new Block[width * height];
		for (int i=0;i<width * height;i++) board[i]=new Block();
		this.nextBlocks = Game.generateNextBlocks(getLevel());
		this.numUndos = 2;
		this.numDeletes = 2;
		this.numMoveAnywhere = 2;
		this.linesCleared = 0;
		this.comboMult = 0;
	}

	public GameState(GameState mGameState) {
		mGameState.previousState = null;
		this.previousState = mGameState;
		if (mGameState != null) {
			this.score = mGameState.score;
			this.numUndos = mGameState.numUndos;
			this.numMoveAnywhere = mGameState.numMoveAnywhere;
			this.numDeletes = mGameState.numDeletes;
			this.board = Common.BlocksCopy(mGameState.board,
					mGameState.board.length);
			this.linesCleared = mGameState.linesCleared;
			this.nextBlocks = Common.BlocksCopy(mGameState.getNextBlocks(),
					mGameState.getNextBlocks().length);
			this.comboMult = mGameState.comboMult;
		}
	}

	public Block[] getNextBlocks() {
		return nextBlocks;
	}

	public void setBoard(Block mBoard[]) {
		for (int i = 0; i < mBoard.length; ++i)
			board[i] = mBoard[i];
	}

	public void setNextBlocks(Block[] nextBlocks) {
		this.nextBlocks = nextBlocks;
	}

	public void generateNextBlocks() {
		this.nextBlocks = Game.generateNextBlocks(getLevel());
	}

	public int getLevel() {
		return 1 + this.linesCleared / 40;
	}

	public int getLevelProgress() {
		return this.linesCleared % 40;
	}

}