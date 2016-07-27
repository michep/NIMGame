package org.michep.nimgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Spinner;

public class StartActivity extends Activity {
	private Spinner spnDifficulty;
	private Spinner spnFirstPlayer;
	private CheckBox chkMisere;
	public final static String EXTRA_GAME_DIFFICULTY = "org.michep.nimgame.gameDifficulty"; 
	public final static String EXTRA_GAME_MISERE = "org.michep.nimgame.gameMisere"; 
	public final static String EXTRA_GAME_FIRST_PLAYER = "org.michep.nimgame.gameFirstPlayer"; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		spnDifficulty = (Spinner) findViewById(R.id.spnDifficulty);
		spnFirstPlayer = (Spinner) findViewById(R.id.spnFirstPlayer);
		chkMisere = (CheckBox) findViewById(R.id.chkMisere);
		
		findViewById(R.id.btnStartGame).setOnClickListener(new ItemImageViewOnClickListener());
	}
	
	private class ItemImageViewOnClickListener implements OnClickListener {

		public void onClick(View v) {
			Intent i = new Intent(getApplicationContext(), GameActivity.class);
			i.putExtra(EXTRA_GAME_DIFFICULTY, spnDifficulty.getSelectedItem().toString());
			i.putExtra(EXTRA_GAME_FIRST_PLAYER, spnFirstPlayer.getSelectedItem().toString());
			if(chkMisere.isChecked())
				i.putExtra(EXTRA_GAME_MISERE, "Misere");
			else
				i.putExtra(EXTRA_GAME_MISERE, "");
			startActivity(i);
			finish();
		}
	}

}
