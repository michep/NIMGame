package org.michep;

import java.util.Random;

public class NimGame {
	private ItemsRow[] rows;
	private String misere;
	private String difficulty;
	private Random rnd = new Random(System.currentTimeMillis() / 1000L);
	public final static String PLAYER_PLAYER="Player";
	public final static String PLAYER_COMPUTER="Computer";
	public final static String MODE_MISERE ="Misere";
	public final static String DIFFICULTY_EASY ="Easy";
	public final static String DIFFICULTY_HARD ="Hard";
	public final static String DIFFICULTY_INSANE ="Insane";

	public NimGame(String difficulty, String misere, int rowslength[]) {
		rows = new ItemsRow[rowslength.length];
		for (int row = 0; row < rowslength.length; row++)
			rows[row] = new ItemsRow(rowslength[row]);
		this.misere = misere;
		this.difficulty = difficulty;
	}

	public String getMisere() {
		return misere;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public int getItemState(int row, int position) {
		return rows[row].getItemState(position);
	}

	public int getItemsCount(int row) {
		return rows[row].getItemsCount();
	}

	public int getExtantCount(int row) {
		return rows[row].getExtantCount();
	}

	public int getExtantRowsCount() {
		int count = 0;
		for (int row = 0; row < rows.length; row++)
			if (rows[row].getExtantCount() > 0)
				count++;
		return count;
	}

	public int getRowsCount() {
		return rows.length;
	}

	public int countItemsInGame() {
		int sum = 0;
		for (int row = 0; row < rows.length; row++)
			sum += rows[row].getExtantCount();
		return sum;

	}

	public void removeItem(int row, int position) {
		rows[row].removeItem(position);
	}

	public boolean isGameOver() {
		for (int row = 0; row < rows.length; row++)
			for (int pos = 0; pos < rows[row].getItemsCount(); pos++)
				if (rows[row].getItemState(pos) == 1)
					return false;
		return true;
	}

	public void removeItemsInRow(int row, int count) {
		int pos = 0;
		int num = 0;
		while (num < count) {
			if (getItemState(row, pos) == 1) {
				removeItem(row, pos);
				// System.out.print(row + 1);
				// System.out.print(" ");
				// System.out.println(pos + 1);
				pos++;
				num++;
				continue;
			}
			pos++;
		}
	}

	public boolean checkMisereMove(int row, int tobreak) {
		for (int rowa = 0; rowa < rows.length; rowa++) {
			if ((rowa == row) && (rows[rowa].getExtantCount() - tobreak) > 1)
				return false;
			if ((rowa != row) && (rows[rowa].getExtantCount()) > 1)
				return false;
		}
		return true;
	}

	public void removeItemsMisere() {
		int tobreak = 0;
		for (int row = 0; row < rows.length; row++) {
			if (rows[row].getExtantCount() > 1) {
				if (getExtantRowsCount() % 2 == 0)
					tobreak = rows[row].getExtantCount();
				else
					tobreak = rows[row].getExtantCount() - 1;
				removeItemsInRow(row, tobreak);
				break;
			}
		}
		if (tobreak == 0)
			removeItemsRandom(1);
	}

	public void removeItemsRandom(int max) {
		int row;
		int tobreak;
		Random rnd = new Random(System.currentTimeMillis() / 1000L);
		while (true) {
			row = rnd.nextInt(rows.length);
			if (getExtantCount(row) > 0)
				break;
		}
		tobreak = rnd.nextInt(getExtantCount(row));
		if (tobreak == 0)
			tobreak++;
		if (tobreak > max && max != 0)
			tobreak = max;
		// System.out.println("random count " + count);
		removeItemsInRow(row, tobreak);
	}

	public void removeItems() {
		int nimsum = 0;
		int tobreak = 0;
		int rowxor;
		for (int row = 0; row < rows.length; row++)
			nimsum = nimsum ^ getExtantCount(row);
		if (nimsum != 0) {
			for (int row = 0; row < rows.length; row++) {
				rowxor = getExtantCount(row) ^ nimsum;
				if (rowxor > getExtantCount(row))
					continue;
				tobreak = getExtantCount(row) - rowxor;
				if (misere.equals(NimGame.MODE_MISERE) && checkMisereMove(row, tobreak)) {
					// System.out.println("MISERE!!!");
					removeItemsMisere();
				} else
					removeItemsInRow(row, tobreak);
				break;
			}
		} else
			removeItemsRandom(1);
	}

	public void removeItemsDifficulty() {
		int level;
		boolean random = false;
		level = rnd.nextInt(100);
		if (difficulty.equals(NimGame.DIFFICULTY_EASY) && level < 70)
			random = true;
		if (difficulty.equals(NimGame.DIFFICULTY_HARD) && level < 40)
			random = true;
		if (random)
			removeItemsRandom(0);
		else
			removeItems();
	}

}
