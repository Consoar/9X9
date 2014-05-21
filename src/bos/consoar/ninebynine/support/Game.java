package bos.consoar.ninebynine.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import bos.consoar.ninebynine.support.entity.Block;

public class Game {
	private static int height = Common.height;
	private static int width = Common.width;
	private GameState mCurrentState;
	public static List<Integer> live_boxs = new ArrayList<Integer>();
	private static int lessBlock = 4;
	private static int[] chaMax = { 1, Common.width, Common.width + 1,
			Common.width - 1 };

	public static List<Integer> getLive_boxs() {
		return live_boxs;
	}

	public static void setLive_boxs(List<Integer> tlive_boxs) {
		live_boxs.clear();
		live_boxs.addAll(tlive_boxs);
	}

	public static int getX(int k) {
		return k / width;
	}

	public static int getY(int k) {
		return k % width;
	}

	public static int getLoction(int x, int y) {
		return x * width + y;
	}

	private static int randomState() {
		return 1 + (int) (5.0D * Math.random());
	}

	private static int randomCenterNum() {
		return 1 + (int) (18.0D * Math.random());
	}

	public static int randomBlockPOS(Block[] mBlocks) {
		int pos = -1;
		while (pos < 0 || mBlocks[pos].getState() != 0) {
			pos = (int) (Math.random() * mBlocks.length);
		}
		return pos;
	}

	public static Block[] generateNextBlocks(int num) {
		int n = Math.min(3 + (num - 1), 6);
		Block[] Blocks = new Block[6];
		for (int i = 0; i < 6; ++i)
			Blocks[i] = new Block();
		for (int i = 0; i < n; ++i)
			Blocks[i] = new Block(randomState(), randomCenterNum());
		return Blocks;
	}

	public GameState getCurrentState() {
		return this.mCurrentState;
	}

	public static Map<Integer, List<Integer>> scanBord(int x, int y,
			Block[] mBlocks, boolean[] boardFlag) {
		Map<Integer, List<Integer>> ans = new HashMap<Integer, List<Integer>>();
		int myPos = getLoction(x, y);
		for (int i = 0; i < 4; ++i) {
			ans.put(i, new ArrayList<Integer>());
			ans.get(i).clear();
		}
		// System.out.println("x "+x+" y "+y);
		boolean l = true, r = true, l1 = true, r1 = true;
		for (int i = 0; i < Common.width; ++i) {
			// line
			if (mBlocks[getLoction(x, i)].getState() == mBlocks[getLoction(x, y)]
					.getState()) {
				// System.out.println("getLoction(x, i) "+getLoction(x,
				// i)+" i "+i);
				if (boardFlag[getLoction(x, i)])
					ans.get(0).add(getLoction(x, i));
			}
			// row
			if (mBlocks[getLoction(i, y)].getState() == mBlocks[getLoction(x, y)]
					.getState()) {
				if (boardFlag[getLoction(i, y)])
					ans.get(1).add(getLoction(i, y));
			}

			// line cross
			if (l1
					&& (x - i) >= 0
					&& (y - i) >= 0
					&& mBlocks[getLoction(x - i, y - i)].getState() == mBlocks[getLoction(
							x, y)].getState()) {
				if (boardFlag[getLoction(x - i, y - i)])
					ans.get(2).add(getLoction(x - i, y - i));
			} else {
				l1 = false;
			}
			if (r1
					&& (x + i) < Common.width
					&& (y + i) < Common.width
					&& mBlocks[getLoction(x + i, y + i)].getState() == mBlocks[getLoction(
							x, y)].getState()) {
				if (i > 0)
					if (boardFlag[getLoction(x + i, y + i)])
						ans.get(2).add(getLoction(x + i, y + i));
			} else {
				r1 = false;
			}

			// row cross
			if (l
					&& (x + i) < Common.width
					&& (y - i) >= 0
					&& mBlocks[getLoction(x + i, y - i)].getState() == mBlocks[getLoction(
							x, y)].getState()) {
				if (boardFlag[getLoction(x + i, y - i)])
					ans.get(3).add(getLoction(x + i, y - i));
			} else {
				l = false;
			}
			if (r
					&& (x - i) >= 0
					&& (y + i) < Common.width
					&& mBlocks[getLoction(x - i, y + i)].getState() == mBlocks[getLoction(
							x, y)].getState()) {
				if (i > 0)
					if (boardFlag[getLoction(x - i, y + i)])
						ans.get(3).add(getLoction(x - i, y + i));
			} else {
				r = false;
			}
		}
		List<Integer> scanList = new ArrayList<Integer>();
		List<Integer> tempList = new ArrayList<Integer>();
		List<Integer> longList = new ArrayList<Integer>();
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 0; i < 4; ++i) {
			scanList = ans.get(i);
			if (scanList.size() < lessBlock) {
				ans.get(i).clear();
				continue;
			}
			tempList.clear();
			longList.clear();
			Collections.sort(scanList);
			map.clear();
			// 统计个元素之间的差值
			for (int j = 1; j < scanList.size(); ++j) {
				int t = scanList.get(j) - scanList.get(j - 1);
				if (t > chaMax[i])
					continue;
				if (!tempList.contains(scanList.get(j) - scanList.get(j - 1)))
					tempList.add(t);
				if (map.containsKey(t)) {
					int count = map.get(t);
					map.put(t, count + 1);
				} else
					map.put(t, 1);
			}
			// 得出出现次数最多的差
			int cha = 0;
			for (int k = 1; k < tempList.size(); ++k) {
				if (map.get(tempList.get(k)) > map.get(tempList.get(cha)))
					cha = k;
			}
			if (cha == 0 && tempList.size() < 1) {
				ans.get(i).clear();
				continue;
			}
			cha = tempList.get(cha);
			for (int k = 1; k < scanList.size(); ++k) {
				if (scanList.get(k) == scanList.get(k - 1) + cha) {
					if (!longList.contains(scanList.get(k)))
						longList.add(scanList.get(k));
					if (!longList.contains(scanList.get(k - 1)))
						longList.add(scanList.get(k - 1));
				} else {
					if (longList.size() < lessBlock)
						longList.clear();
					else
						break;
				}
			}
			ans.get(i).clear();
			Collections.sort(longList);
			ans.get(i).addAll(longList);
			if (longList.size() < lessBlock || cha > Common.height + 1)
				ans.get(i).clear();
		}
		return ans;
	}

	@SuppressWarnings("resource")
	public static void save(Context context, GameState mGameState)
			throws FileNotFoundException, IOException {
		ObjectOutputStream oos = null;
		System.out.println("save");
		oos = new ObjectOutputStream(new FileOutputStream(new File(
				context.getFilesDir(), "GameSave.dat")));
		oos.writeObject(mGameState);
		oos.flush();
	}

	@SuppressWarnings("resource")
	public static GameState load(Context context)
			throws StreamCorruptedException, FileNotFoundException,
			IOException, ClassNotFoundException {
		System.out.println("load");
		ObjectInputStream ois = null;
		ois = new ObjectInputStream(new FileInputStream(new File(
				context.getFilesDir(), "GameSave.dat")));
		GameState mGameState = (GameState) ois.readObject();
		System.out.println(mGameState);
		return mGameState;
	}

	public boolean isEmptyBoard(Block[] board) {
		for (int i = 0; i < board.length; ++i)
			if (board[i].getState() != 0)
				return false;
		return true;
	}

	public static void clear(Context context) {
		File localFile = new File(context.getFilesDir(), "GameSave.dat");
		if (localFile.exists())
			localFile.delete();
	}

	public boolean isGameOver() {
		GameState mGameState = getCurrentState();
		if (mGameState.leftBlocks == 0)
			return true;
		return false;
	}

	public static List<Integer> getValidMoves(Block[] mBlocks, int x, int y,
			boolean flag) {
		int tempDirection[][] = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };
		for (int i = 0; i < tempDirection.length; ++i) {
			int[] temp = tempDirection[i];
			if ((x + temp[0]) >= 0
					&& (x + temp[0]) <= Common.height - 1
					&& (y + temp[1]) >= 0
					&& (y + temp[1]) <= Common.width - 1
					&& !live_boxs
							.contains(getLoction(x + temp[0], y + temp[1]))
					&& mBlocks[getLoction(x + temp[0], y + temp[1])].getState() == 0) {
				live_boxs.add(getLoction(x + temp[0], y + temp[1]));
				getValidMoves(mBlocks, x + temp[0], y + temp[1], false);
			}
		}
		if (flag)
			return live_boxs;
		return null;
	};
}
