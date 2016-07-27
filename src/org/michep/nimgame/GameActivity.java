package org.michep.nimgame;

import org.michep.NimGame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameActivity extends Activity {
	protected LinearLayout[] layRows = new LinearLayout[3];
	protected String gameDifficulty;
	protected String gameFirstPlayer;
	protected String gameMisere;
	protected TextView txtHeader;
	protected TextView txtHint;
	protected boolean isFirstAction;
	protected Button btnEndTurn;
	protected String currentPlayer;
	protected NimGame game;
	protected Handler handler = new Handler(new CallbackHandler());
	protected Resources res;
	protected static int gameState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	    //Remove title bar
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    
		setContentView(R.layout.activity_game);

		txtHeader = (TextView) findViewById(R.id.txtCurrentMove);
		txtHint = (TextView) findViewById(R.id.txtHint);

		layRows[0] = (LinearLayout) findViewById(R.id.layRow1);
		layRows[1] = (LinearLayout) findViewById(R.id.layRow2);
		layRows[2] = (LinearLayout) findViewById(R.id.layRow3);
		
		btnEndTurn = (Button) findViewById(R.id.btnEndTurn);
		btnEndTurn.setOnClickListener(new ButtonOnClickListener());
		
		res = getResources();

		initItemsInGame();
		
		setHeader();
		setHint();
		gameState = 0;
		
		if(currentPlayer.equals(NimGame.PLAYER_COMPUTER))
		{
			btnEndTurn.setEnabled(false);
			setAllItemsAccess(false);
			handler.sendEmptyMessageDelayed(1, 1000);
		}
	}
	
	protected void initItemsInGame() {
		ImageView v;
		int rowW;
		int itemW;

		Resources res = getResources();
		Drawable d = res.getDrawable(R.drawable.star_white);
		Drawable db = res.getDrawable(R.drawable.star_contour);


		Point p = new Point();
		getWindowManager().getDefaultDisplay().getSize(p);
		rowW = p.x;
		itemW = (int) Math.floor(rowW / 6);
		
		gameDifficulty = getIntent().getStringExtra("org.michep.nimgame.gameDifficulty");
		gameMisere = getIntent().getStringExtra("org.michep.nimgame.gameMisere");
		currentPlayer = getIntent().getStringExtra("org.michep.nimgame.gameFirstPlayer");
		
		game = new NimGame(gameDifficulty, gameMisere, new int[] {6, 4, 3});
		
		for (int row = 0; row < game.getRowsCount(); row++) {
			for (int pos = 0; pos < game.getItemsCount(row); pos++) {
				v = new ItemImageView(getApplicationContext(), d, db, itemW, row, pos, game);
				layRows[row].addView(v);				
			}
			layRows[row].requestLayout();
		}
	}
	
	protected void setHeader() {
		if(currentPlayer.equals(NimGame.PLAYER_COMPUTER))
			txtHeader.setText(R.string.computer_turn);

		if(currentPlayer.equals(NimGame.PLAYER_PLAYER))
			txtHeader.setText(R.string.player_turn);
	}
	
	protected void setHint() {
		if(gameMisere.equals(NimGame.MODE_MISERE))
			txtHint.setText(R.string.rules_misere_tip);
		else
			txtHint.setText(R.string.rules_tip);
	}
	
	protected void computerTurn() {
		game.removeItemsDifficulty();
		setAllItemsState();
		isGameOver();
		nextPlayer();				
		setHeader();
		btnEndTurn.setEnabled(true);
		setAllItemsAccess(true);
	}

	protected void isGameOver() {
		if(game.isGameOver()) {
			gameState = 1;
			if(gameMisere.equals(NimGame.MODE_MISERE))
				nextPlayer();
			DialogOnClickListener dialogListener = new DialogOnClickListener();
			AlertDialog.Builder builder = new AlertDialog.Builder(this)
				.setCancelable(false)
				.setTitle(R.string.game_over)
				.setMessage(currentPlayer + res.getString(R.string.is_the_winner))
				.setPositiveButton(R.string.play_again, dialogListener)
				.setNegativeButton(R.string.exit, dialogListener);
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}
	
	protected void nextPlayer() {
		currentPlayer = currentPlayer.equals(NimGame.PLAYER_COMPUTER) ? NimGame.PLAYER_PLAYER : NimGame.PLAYER_COMPUTER;
	}
	
	protected void setAllItemsState () {
		ItemImageView v;
		for (int row = 0; row < game.getRowsCount(); row++)
			for (int pos = 0; pos < game.getItemsCount(row); pos++)
				if(game.getItemState(row, pos) == 0) {
					v = (ItemImageView)layRows[row].getChildAt(pos);
					v.setItemImageBroken();
				}
	}
	
	protected void setAllItemsAccess (boolean enabled) {
		ItemImageView v;
		for (int row = 0; row < game.getRowsCount(); row++)
			for (int pos = 0; pos < game.getItemsCount(row); pos++) {
					v = (ItemImageView)layRows[row].getChildAt(pos);
					v.setEnabled(enabled);
			}
	}
	
	private class ButtonOnClickListener implements OnClickListener {
		public void onClick(View v) {
			if(ItemImageViewOnClickListener.getIsFirstMove())
				return;
			isGameOver();
			nextPlayer();
			setHeader();
			if(currentPlayer.equals(NimGame.PLAYER_COMPUTER))
			{
				btnEndTurn.setEnabled(false);
				setAllItemsAccess(false);
				handler.sendEmptyMessageDelayed(1, 1000);
			}
			ItemImageViewOnClickListener.reset();
		}
	}
	
	private class DialogOnClickListener implements DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			switch(which) {
			case(-2):
				finish();
				dialog.dismiss();
				break;
			case(-1):
				Intent i = new Intent(getApplicationContext(), StartActivity.class);
				startActivity(i);
				finish();
				dialog.dismiss();
				break;
			}
		}
	}
	
	private class CallbackHandler implements Callback {
		public boolean handleMessage(Message msg) {
            if (msg.what == 1 && GameActivity.gameState == 0) {
				computerTurn();
				return true;
            }
            return false;
		}
	}
}
