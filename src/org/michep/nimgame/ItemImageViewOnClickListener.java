package org.michep.nimgame;

import android.view.View;
import android.view.View.OnClickListener;

public class ItemImageViewOnClickListener implements OnClickListener {
	private static boolean isFirstMove = true;
	private static int firstRow = 0;

	public ItemImageViewOnClickListener() {
	}

	public static void reset() {
		isFirstMove = true;
	}

	public static boolean getIsFirstMove() {
		return isFirstMove;
	}
	
	public void onClick(View v) {
		ItemImageView iv = (ItemImageView) v;
		
		if (isFirstMove) {
			firstRow = iv.getRow();
			isFirstMove = false;
		}

		if (!isFirstMove && iv.getRow() != firstRow)
			return;
		
		if(iv.getState() == 1) {
			iv.setItemImageBroken();
		}
	}
}
