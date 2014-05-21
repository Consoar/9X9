package bos.consoar.ninebynine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;
import android.widget.Toast;
import bos.consoar.ninebynine.support.Common;
import bos.consoar.ninebynine.support.GBTools;
import bos.consoar.ninebynine.support.Game;
import bos.consoar.ninebynine.support.GameSharedPreferencesTool;
import bos.consoar.ninebynine.support.GameState;
import bos.consoar.ninebynine.support.entity.Block;
import bos.consoar.ninebynine.views.ActionBar;
import bos.consoar.ninebynine.views.ActionBar.Action;
import bos.consoar.ninebynine.views.ActionBarToggle;
import bos.consoar.ninebynine.views.BadgeView;
import bos.consoar.ninebynine.views.BoardView;
import bos.consoar.ninebynine.views.UpNextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;

import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import eu.inmite.android.lib.dialogs.SimpleDialogFragment;

public class GameActivity extends FragmentActivity implements
		ISimpleDialogListener {
	private static final String TAG = "GameActivity";
	private static final int MENU_NEWGAME = Menu.FIRST;
	private static final int MENU_ANYWAY = 0;
	private static final int MENU_DELETE = 1;
	private static final int MENU_UNDO = 2;
	private Game mGame;
	private GameState mGameState, mHisGameState;
	private UpNextView mUpNextView;
	private BoardView mBoardView;
	private ActionBar mActionBar;
	private ActionBarToggle View_AnyWay, View_Delete, View_Undo;
	private BadgeView AnyWay_bv, Delete_bv, Undo_bv;
	private int srcBlock = -1, bforeNowBlock = -1;
	private int srcState, srcNum;
	private TextView score_TextView, combo_TextView, nextLevel_TextView;
	private List<Integer> live_box_all = new ArrayList<Integer>();
	private boolean isClean;
	private boolean game_over_exit;
	private int nowLevel = 1;
	private LinkedList<Integer> touchPath = new LinkedList<Integer>();
	private boolean[] boardFlag = new boolean[Common.height * Common.width];
	private boolean isDeadDialogOpen = false;
	
	private void disAnyWay() {
		View_AnyWay.setChecked(true);
		View_AnyWay.setEnabled(false);
	}

	private void openAnyWay() {
		View_AnyWay.setChecked(false);
		View_AnyWay.setEnabled(true);
	}

	private void disDelete() {
		View_Delete.setChecked(true);
		View_Delete.setEnabled(false);
	}

	private void openDelete() {
		View_Delete.setChecked(false);
		View_Delete.setEnabled(true);
	}

	private void disUndo() {
		View_Undo.setChecked(true);
		View_Undo.setEnabled(false);
	}

	private void openUndo() {
		View_Undo.setChecked(false);
		View_Undo.setEnabled(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		findViews();
		setListener();
		addActionbar();
		init();
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	protected void onStart() {
		super.onStart();
		regBroadcastRecv();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (!game_over_exit) {
			try {
				mGameState.board = mBoardView.getBoard();
				Game.save(this, mGameState);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unRegBroadcastRecv();
	}

	private void findViews() {
		mUpNextView = (UpNextView) findViewById(R.id.up_next);
		mBoardView = (BoardView) findViewById(R.id.main_BoardView);
		score_TextView = (TextView) findViewById(R.id.score);
		combo_TextView = (TextView) findViewById(R.id.combo);
		nextLevel_TextView = (TextView) findViewById(R.id.next_level_lines_label);
	}

	
	private void init() {
		mGame = new Game();
		game_over_exit = false;
		isDeadDialogOpen = false;
		boolean havaHis = true;
		try {
			mHisGameState = Game.load(this);
			System.out.println(mHisGameState);
			if (mHisGameState != null)
				mGameState = new GameState(mHisGameState);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			havaHis = false;
			e.printStackTrace();
		} catch (IOException e) {
			havaHis = false;
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			havaHis = false;
			e.printStackTrace();
		}
		if (!havaHis) {
			mGameState = new GameState();
			mBoardView.setBoard(mGameState.board);
		}
		live_box_all.clear();
		for (int i = 0; i < Common.width * Common.height; ++i)
			live_box_all.add(i);
		AnyWay_bv.setText(String.valueOf(mGameState.numMoveAnywhere));
		Undo_bv.setText(String.valueOf(mGameState.numUndos));
		Delete_bv.setText(String.valueOf(mGameState.numDeletes));
		score_TextView.setText(String.valueOf(mGameState.score));
		combo_TextView.setText(String.valueOf(mGameState.comboMult));
		nextLevel_TextView.setText(getLevelProgress());
		mActionBar.setTitle(getNowLevel());
		if (!havaHis || mGame.isEmptyBoard(mGameState.board)) {
			generateNextBlocks();
			placeNextBlocks();
		} else {
			mBoardView.setBoard(mGameState.board);
			mUpNextView.setStates(mGameState.getNextBlocks());
		}
		resetItem();
		checkItem();
	}

	private void resetItem() {
		View_AnyWay.setChecked(false);
		View_Delete.setChecked(false);
		View_Undo.setChecked(false);
	}

	private void checkItem() {
		if (View_AnyWay.isChecked() && mGameState.numMoveAnywhere != 0) {
			View_AnyWay.setChecked(false);
			mGameState.numMoveAnywhere--;
			AnyWay_bv.setText(String.valueOf(mGameState.numMoveAnywhere));
		}
		if (mGameState.numMoveAnywhere == 0) {
			View_AnyWay.setEnabled(false);
			View_AnyWay.setChecked(true);
		}
		if (mGameState.numMoveAnywhere > 0) {
			openAnyWay();
		}
		if (View_Delete.isChecked() && mGameState.numDeletes != 0) {
			View_Delete.setChecked(false);
			mGameState.numDeletes--;
			Delete_bv.setText(String.valueOf(mGameState.numDeletes));
		}
		if (mGameState.numDeletes == 0) {
			View_Delete.setEnabled(false);
			View_Delete.setChecked(true);
		}
		if (mGameState.numDeletes > 0) {
			openDelete();
		}
	}

	private void undo() {
		if (mGameState.numUndos == 0) {
			disUndo();
			return;
		}
		if (mGameState.previousState == null)
			return;
		mGameState = new GameState(mGameState.previousState);
		mBoardView.setBoard(mGameState.board);
		mUpNextView.setStates(mGameState.getNextBlocks());
		AnyWay_bv.setText(String.valueOf(mGameState.numMoveAnywhere));
		Undo_bv.setText(String.valueOf(mGameState.numUndos));
		Delete_bv.setText(String.valueOf(mGameState.numDeletes));
		score_TextView.setText(String.valueOf(mGameState.score));
		combo_TextView.setText(String.valueOf(mGameState.comboMult));
		nextLevel_TextView.setText(getLevelProgress());
		mActionBar.setTitle(getNowLevel());
		mGameState.numUndos--;
		Undo_bv.setText(String.valueOf(mGameState.numUndos));
		disUndo();
	}

	private String getLevelProgress() {
		String sLevelProgress = getResources().getString(R.string.next_level);
		return String
				.format(sLevelProgress, 40 - mGameState.getLevelProgress());
	}

	private String getNowLevel() {
		String sNowLevel = getResources().getString(R.string.now_level);
		return String.format(sNowLevel, mGameState.getLevel());
	}

	private void setListener() {
		mBoardView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int k = mBoardView.getLocation(event);
				// System.out.println("k " + k);
				if (k < 0) {
					clearEmptyGirdState();
					if (srcBlock >= 0) {
						mBoardView.setAllBlockViewsPressed(false);
						mBoardView.setBlockViewNum(srcBlock, srcNum);
					}
					return false;
				}
				switch (event.getAction()) {

				case MotionEvent.ACTION_DOWN: {
					srcBlock = k;
					bforeNowBlock = k;
					srcState = mBoardView.getBlockViewState(k);
					srcNum = mBoardView.getBlockViewNum(k);
					touchPath.clear();
					touchPath.add(k);
					if (mBoardView.mBlockViews[k].getState() == 0)
						return false;
					Game.live_boxs.clear();
					List<Integer> live_box = Game.getValidMoves(
							mBoardView.getBoard(), Game.getX(k), Game.getY(k),
							true);
					if (View_AnyWay.isChecked()
							&& mGameState.numMoveAnywhere > 0) {
						mBoardView.setValidBlock(live_box_all);
					} else {
						mBoardView.setValidBlock(live_box);
					}
					// 按住事件发生后执行代码的区域
				}
				case MotionEvent.ACTION_MOVE: {
					// 移动事件发生后执行代码的区域
					// 移动到无效区域
					if (k < 0 || srcBlock < 0)
						break;
					// 如果将将指头移回原来的方块，或者移到无法移动的地方
					if ((!View_AnyWay.isChecked() && !Game.live_boxs
							.contains(k)) || (k == srcBlock)) {
						mBoardView.clearPath();
						mBoardView.setBlockViewSelected(bforeNowBlock, false);
						mBoardView.setBlockViewPressed(bforeNowBlock, false);
						bforeNowBlock = k;
						if (k == srcBlock) {
							touchPath.clear();
							touchPath.add(srcBlock);
							mBoardView.setAllBlockViewsPressed(false);
							mBoardView.setAllBlockViewsSelected(false);
							mBoardView.setBlockViewNum(srcBlock, srcNum);
						}
						break;
					}
					// 如果移动到空白方块
					if ((mBoardView.getBlockViewState(k) == 0 || View_AnyWay
							.isChecked())) {
						// 一些初始化
						if (k >= 0 && bforeNowBlock != k) {
							bforeNowBlock = k;
						}
						// 设置路径
						if ((bforeNowBlock >= 0
								&& srcBlock >= 0
								&& (mBoardView.getBlockViewState(bforeNowBlock) == 0)
								&& (Game.live_boxs.contains(bforeNowBlock)) || View_AnyWay
								.isChecked())) {
							int lastpos = touchPath.getLast();
							if (Math.abs(lastpos - bforeNowBlock) != 9
									&& Math.abs(lastpos - bforeNowBlock) != 1
									&& Math.abs(lastpos - bforeNowBlock) != 0)
								break;
							if (mBoardView.getBlockViewNum(srcBlock) - 1 < 0
									&& !touchPath.contains(bforeNowBlock))
								break;
							mBoardView.setBlockViewNum(srcBlock, srcNum
									- touchPath.size() + 1);
							if (!touchPath.contains(bforeNowBlock)
									&& mBoardView.getBlockViewNum(srcBlock) != 0) {
								touchPath.add(bforeNowBlock);
								mBoardView.setBlockViewNum(
										srcBlock,
										mBoardView.getBlockViewNum(srcBlock) - 1);
							} else {
								int last = touchPath.indexOf(bforeNowBlock);
								if (last >= 0)
									for (int t = touchPath.size() - 1; t > last; --t) {
										mBoardView.setBlockViewSelected(
												touchPath.get(t), false);
										mBoardView.setBlockViewPressed(
												touchPath.get(t), false);
										touchPath.remove(t);
										mBoardView
												.setBlockViewNum(
														srcBlock,
														mBoardView
																.getBlockViewNum(srcBlock) + 1);
										Integer[] path = (Integer[]) touchPath
												.toArray(new Integer[0]);
										mBoardView.setPath(srcState, srcBlock,
												bforeNowBlock, path);
									}
							}
							if (mBoardView.getBlockViewNum(srcBlock) < 0)
								break;
							Integer[] path = (Integer[]) touchPath
									.toArray(new Integer[0]);
							mBoardView.setPath(srcState, srcBlock,
									bforeNowBlock, path);
						}
					}
				}
					break;
				case MotionEvent.ACTION_UP: {
					// 松开事件发生后执行代码的区域
				}
				case MotionEvent.ACTION_CANCEL: {
					System.out.println("ACTION_CANCEL");

					if (View_Delete.isChecked() && mGameState.numDeletes > 0
							&& srcBlock == bforeNowBlock) {
						mBoardView.mBlockViews[k].setState(0);
						mBoardView.clearPath();
						mBoardView.setAllBlockViewsEnabled(true);
						mBoardView.setBlockViewSelected(k, false);
						mBoardView.setBlockViewPressed(k, false);
						if (mGameState.numUndos > 0)
							openUndo();
						GameState GameState1 = mGameState;
						GameState tGameState = new GameState(GameState1);
						mGameState = tGameState;
						checkItem();
						break;
					}
					// 如果将将指头移回原来的方块，或者移到无法移动的地方
					if (((!View_AnyWay.isChecked() && !Game.live_boxs
							.contains(k)) || !touchPath.contains(bforeNowBlock))
							|| (bforeNowBlock == srcBlock)) {
						clearEmptyGirdState();
						mBoardView.setBlockViewNum(srcBlock, srcNum);
						break;
					}
					// 进行移动
					if (bforeNowBlock >= 0 && srcBlock != bforeNowBlock
							&& mBoardView.getBlockViewState(bforeNowBlock) == 0) {
						clearEmptyGirdState();
						mBoardView.setAllBlockViewsPressed(false);
						mBoardView.setAllBlockViewsSelected(false);

						mBoardView.setBlockViewNum(srcBlock, srcNum);
						move(srcBlock, bforeNowBlock);
						doClean(bforeNowBlock);
						if (!isGameOver() && !isClean)
							placeNextBlocks();
					}
					break;
				}
				default:
					break;
				}
				mGameState.board = mBoardView.getBoard();
				return true;
			}
		});
	}

	private void doClean(int pos) {
		LinkedList<Animator> mLinkedList = new LinkedList<Animator>();
		AnimatorSet mAnimatorSet = new AnimatorSet();
		List<Integer> lines = new ArrayList<Integer>();
		Arrays.fill(boardFlag, true);
		Map<Integer, List<Integer>> tBlocks = Game.scanBord(Game.getX(pos),
				Game.getY(pos), mBoardView.getBoard(), boardFlag);
		for (int t = 0; t < 4; ++t) {
			lines = tBlocks.get(t);
			for (int j = 0; j < lines.size(); ++j) {
				boardFlag[lines.get(j)] = false;
				mLinkedList.add(mBoardView.getAnimeStateSet(lines.get(j),
						new Block(0, 0)));
			}
		}
		clearUpBlocks(tBlocks, false);
		mAnimatorSet.playTogether(mLinkedList);
		mAnimatorSet.start();
	}

	private void move(int src, int dst) {
		if (mGameState.numUndos > 0)
			openUndo();
		mBoardView.setBlockViewNum(src, srcNum);
		mGameState.board = mBoardView.getBoard();
		GameState GameState1 = mGameState;
		GameState tGameState = new GameState(GameState1);
		mGameState = tGameState;
		mBoardView.mBlockViews[dst].setState(mBoardView.mBlockViews[src]
				.getState());
		mBoardView.mBlockViews[dst].setCenterNum(mBoardView.mBlockViews[src]
				.getCenterNum());
		mBoardView.mBlockViews[src].setState(0);
		mBoardView.mBlockViews[src].setCenterNum(0);
		checkItem();
	}

	public void clearEmptyGirdState() {
		mBoardView.clearPath();
		mBoardView.setAllBlockViewsPressed(false);
		mBoardView.setAllBlockViewsSelected(false);
		mBoardView.setAllBlockViewsEnabled(true);
	}

	public void startNewGame() {
		Game.clear(this);
		mBoardView.clearPath();
		mBoardView.setAllBlockViewsEnabled(true);
		init();
	}

	public boolean isGameOver() {
		Block[] board = mBoardView.getBoard();
		for (int i = 0; i < Common.height * Common.width; ++i)
			if (board[i].getState() == 0)
				return false;
		return true;
	}

	private void openDeadDialog() {
		int highsocre = GameSharedPreferencesTool
				.getHighSocre(getApplicationContext());
		if (mGameState.score > highsocre) {
			highsocre = mGameState.score;
			GameSharedPreferencesTool.setHighSocre(getApplicationContext(),
					highsocre);
		}
		String sDeadMessage = getResources().getString(R.string.DEAD_MESSAGE);
		sDeadMessage = String.format(sDeadMessage, highsocre, mGameState.score);
		SimpleDialogFragment
				.createBuilder(getApplicationContext(),
						getSupportFragmentManager())
				.setTitle(R.string.GAMEOVER).setMessage(sDeadMessage)
				.setPositiveButtonText(R.string.EXIT)
				.setNegativeButtonText(R.string.NEW_GAME).setRequestCode(1)
				.show();
	}

	public void onPositiveButtonClicked(int requestCode) {
		game_over_exit = true;
		Game.clear(this);
		GameActivity.this.finish();
	}

	public void onNegativeButtonClicked(int requestCode) {
		if (requestCode == 1) {
			startNewGame();
		}
	}

	private void generateNextBlocks() {
		mGameState.generateNextBlocks();
		mUpNextView.setStates(mGameState.getNextBlocks());
	}

	private void placeNextBlocks() {
		Map<Integer, List<Integer>> tBlocks = null;
		Block[] mBlocks = mGameState.nextBlocks;
		Arrays.fill(boardFlag, true);
		LinkedList<Animator> mLinkedList = new LinkedList<Animator>();
		AnimatorSet mAnimatorSet = new AnimatorSet();
		List<Integer> lines = new ArrayList<Integer>();
		Block[] mBoard;
		for (int i = 0; i < mBlocks.length; ++i) {
			if (mBlocks[i].getState() == 0)
				continue;
			mBoard = mBoardView.getBoard();
			int tpos = Game.randomBlockPOS(mBoard);
			mBoardView.setBlockViewState(tpos, mBlocks[i].getState());
			mBoardView.setBlockViewNum(tpos, mBlocks[i].getNum());
			GBTools.GBsetAlpha(mBoardView.mBlockViews[tpos], 0.0F);
			mLinkedList.add(mBoardView.getAnimeStateSet(tpos, mBlocks[i]));
			tBlocks = Game.scanBord(Game.getX(tpos), Game.getY(tpos),
					mBoardView.getBoard(), boardFlag);
			AnimatorSet mAnimatorSet2 = new AnimatorSet();
			LinkedList<Animator> mLinkedList2 = new LinkedList<Animator>();
			for (int t = 0; t < 4; ++t) {
				lines = tBlocks.get(t);
				for (int j = 0; j < lines.size(); ++j) {
					boardFlag[lines.get(j)] = false;
					mLinkedList2.add(mBoardView.getAnimeStateSet(lines.get(j),
							new Block(0, 0)));
				}
			}
			mAnimatorSet2.playTogether(mLinkedList2);
			mLinkedList.add(mAnimatorSet2);
			clearUpBlocks(tBlocks, true);
			if (isGameOver())
				break;
		}
		mAnimatorSet.playSequentially(mLinkedList);
		mAnimatorSet.start();
		generateNextBlocks();
	}

	public void clearUpBlocks(Map<Integer, List<Integer>> mBlocks,
			boolean isNewBlock) {
		// TODO Auto-generated method stub
		boolean moreAnyWay = false, moreDelete = false, moreUndo = false;
		int totlines, extraBlocks;
		boolean flag = false;
		List<Integer> lines = new ArrayList<Integer>();
		totlines = 0;
		extraBlocks = 0;
		isClean = false;
		for (int i = 0; i < 4; ++i) {
			lines = mBlocks.get(i);
			if (lines.size() > 0) {
				flag = true;
				isClean = true;
				totlines++;
				if (lines.size() > 4)
					extraBlocks = extraBlocks + lines.size() - 4;
			}
		}
		// 算连击
		if (isNewBlock) {
			if (flag)
				mGameState.comboMult++;
		} else {
			if (flag)
				mGameState.comboMult++;
			else {
				mGameState.comboMult = 0;
				moreAnyWay = false;
				moreDelete = false;
				moreUndo = false;
			}
		}
		combo_TextView.setText(String.valueOf(mGameState.comboMult));
		// 算分
		int tempScore = (20 * totlines + 8 * extraBlocks) * totlines
				* mGameState.comboMult * mGameState.getLevel();
		mGameState.score = mGameState.score + tempScore;
		score_TextView.setText(String.valueOf(mGameState.score));
		mGameState.linesCleared += totlines;
		// 算奖励道具
		if (totlines >= 2 && !moreAnyWay) {
			// 奖励AnyWay
			moreAnyWay = true;
			mGameState.numMoveAnywhere++;
			AnyWay_bv.setText(String.valueOf(mGameState.numMoveAnywhere));
		}
		if (mGameState.comboMult >= 4 && !moreDelete) {
			// 奖励Deletes
			moreDelete = true;
			mGameState.numDeletes++;
			Delete_bv.setText(String.valueOf(mGameState.numDeletes));
		}
		if (mGameState.comboMult >= 5 && !moreUndo) {
			// 奖励Undo
			moreUndo = true;
			mGameState.numUndos++;
			Undo_bv.setText(String.valueOf(mGameState.numUndos));
		}
		nextLevel_TextView.setText(getLevelProgress());
		if (mGameState.getLevel() > nowLevel) {
			nowLevel = mGameState.getLevel();
			mGameState.numUndos++;
			Undo_bv.setText(String.valueOf(mGameState.numUndos));
			mActionBar.setTitle(getNowLevel());
		}
		mActionBar.setTitle(getNowLevel());
		if (mGame.isEmptyBoard(mBoardView.getBoard())) {
			placeNextBlocks();
		}
		checkItem();
	}

	private void addActionbar() {
		// TODO Auto-generated method stub
		mActionBar = (ActionBar) findViewById(R.id.actionbar);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setHomeAction(new Action() {

			@Override
			public int getDrawable() {
				// TODO Auto-generated method stub
				return R.drawable.ic_launcher;
			}

			@Override
			public void performAction(View view) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mActionBar.setTitleColor(Color.BLACK);
		mActionBar.addAction(new Action() {

			@Override
			public void performAction(View view) {
				// TODO Auto-generated method stub
				Toast.makeText(GameActivity.this, "任意移动", Toast.LENGTH_SHORT)
						.show();
				if (View_AnyWay.isChecked())
					View_AnyWay.setChecked(false);
				else
					View_AnyWay.setChecked(true);
			}

			@Override
			public int getDrawable() {
				// TODO Auto-generated method stub
				return R.drawable.ic_move_anywhere;
			}
		}, MENU_ANYWAY);
		View_AnyWay = (ActionBarToggle) mActionBar.getAction(MENU_ANYWAY)
				.findViewById(R.id.actionbar_item);
		AnyWay_bv = getBadgeView(mActionBar.getAction(MENU_ANYWAY));
		AnyWay_bv.show();
		mActionBar.addAction(new Action() {

			@Override
			public void performAction(View view) {
				// TODO Auto-generated method stub
				if (View_Delete.isChecked())
					View_Delete.setChecked(false);
				else
					View_Delete.setChecked(true);
				Toast.makeText(GameActivity.this, "消除", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public int getDrawable() {
				// TODO Auto-generated method stub
				return R.drawable.ic_delete;
			}
		}, MENU_DELETE);
		View_Delete = (ActionBarToggle) mActionBar.getAction(MENU_DELETE)
				.findViewById(R.id.actionbar_item);
		Delete_bv = getBadgeView(mActionBar.getAction(MENU_DELETE));
		Delete_bv.show();
		mActionBar.addAction(new Action() {

			@Override
			public void performAction(View view) {
				// TODO Auto-generated method stub
				undo();
				Toast.makeText(GameActivity.this, "撤销", Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public int getDrawable() {
				// TODO Auto-generated method stub
				return R.drawable.ic_undo;
			}
		}, MENU_UNDO);
		View_Undo = (ActionBarToggle) mActionBar.getAction(MENU_UNDO)
				.findViewById(R.id.actionbar_item);
		Undo_bv = getBadgeView(mActionBar.getAction(MENU_UNDO));
		Undo_bv.show();
	}

	private BadgeView getBadgeView(View view) {
		BadgeView mBadgeView;
		mBadgeView = new BadgeView(this, view);
		mBadgeView.setBadgeBackgroundColor(getResources().getColor(
				android.R.color.transparent));
		mBadgeView.setTextSize(10f);
		mBadgeView.setTextColor(Color.GRAY);
		mBadgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		return mBadgeView;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(Menu.NONE, MENU_NEWGAME, 1,
				getResources().getString(R.string.NEW_GAME));
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_NEWGAME:
			startNewGame();
			break;
		}
		// 返回true表示处理完菜单项的事件，不需要将该事件继续传播下去了
		return true;
	}

	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("bos.consoar.ninebynine.GameIsOver")) {
				if (isGameOver() && !isDeadDialogOpen) {
					isDeadDialogOpen = true;
					openDeadDialog();
				}
			}
		}
	};

	// 广播接收器注册
	private void regBroadcastRecv() {
		IntentFilter intentFilter = new IntentFilter(
				"bos.consoar.ninebynine.GameIsOver");
		this.registerReceiver(mReceiver, intentFilter);
	}

	// 广播接收器注册
	private void unRegBroadcastRecv() {
		this.unregisterReceiver(mReceiver);
	}
}
