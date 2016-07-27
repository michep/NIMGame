package org.michep;

import java.io.Console;

public class GameController {
	private NimGame game;
	private Console console = System.console();
	private String player;

	public GameController(String player, NimGame game) {
		this.game = game;
		this.player = player;
	}

	public void printItems() {
		for (int row = 0; row < game.getRowsCount(); row++) {
			for (int pos = 0; pos < game.getItemsCount(row); pos++) {
				System.out.print(game.getItemState(row, pos));
				System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public void printNim() {
		int nimsum = 0;
		for (int row = 0; row < game.getRowsCount(); row++) {
			System.out.println(String.format("%8s", Integer.toBinaryString(game.getExtantCount(row))).replace(' ', '0'));
			nimsum = nimsum ^ game.getExtantCount(row);
		}
		System.out.println(String.format("%8s", Integer.toBinaryString(nimsum)).replace(' ', '0'));
		System.out.println();
	}

	public void nextPlayer() {
		player = (player.equals("user") ? "computer" : "user");
	}

	public String getPlayer() {
		return player;
	}

	public void playerBonesBreak() {
		int row = 0;
		int position = 0;
		String str = "0";
		while (true) {
			if (str.isEmpty())
				break;
			str = ReadLine("Brake a bone in which row?");
			System.out.println(str);
			row = Integer.parseInt(str);
			if (game.getExtantCount(row - 1) == 0) {
				System.out.println("Empty row!");
				continue;
			}
			while (true) {
				str = ReadLine("Which bone?");
				if (str.isEmpty())
					break;
				System.out.println(str);
				position = Integer.parseInt(str);
				if (game.getItemState(row - 1, position - 1) == 0) {
					System.out.println("Already broken bone!");
					continue;
				}
				game.removeItem(row - 1, position - 1);
			}
		}
	}

	private String ReadLine(String question) {
		if (question != null)
			System.out.print(question + " ");
		String answer = console.readLine();
		// String answer = "1";
		return answer;
	}

	public static void main(String[] args) {
		NimGame game;
		if (args.length == 2)
			game = new NimGame(args[0], args[1], new int[] {11, 7, 5, 3});
		else
			game = new NimGame("hard", "misere", new int[] {6, 4, 3});

		System.out.println(game.getMisere());
		System.out.println(game.getDifficulty());

		GameController ctrl = new GameController("user", game);

		while (!game.isGameOver()) {

			ctrl.printItems();
			ctrl.printNim();

			if (ctrl.player.equals("user")) {
				// game.removeItems();
				ctrl.playerBonesBreak();
			}
			if (ctrl.player.equals("computer")) {
				game.removeItemsDifficulty();
				// ctrl.playerBonesBreak();
			}
			ctrl.nextPlayer();
		}
		ctrl.printItems();
		if (!(game.getMisere().equals("misere")))
			ctrl.nextPlayer();
		System.out.println("Game Over! The winner is " + ctrl.player);
	}
}
