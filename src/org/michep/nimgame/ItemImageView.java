package org.michep.nimgame;

import org.michep.NimGame;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class ItemImageView extends ImageView {
	private Drawable drItem;
	private Drawable drItemBroken;
	private int itemWidth;
	private int state = 1;
	private NimGame game;
	private int row;
	private int pos;

	public ItemImageView(Context context, Drawable d, Drawable db, int width, int row, int pos, NimGame game) {
		super(context);
		this.drItem = d;
		this.drItemBroken = db;
		this.itemWidth = width;
		this.game = game;
		this.row = row;
		this.pos = pos;
		initItemImageView();
	}
	
	private void initItemImageView() {
		setImageDrawable(drItem);
		setLayoutParams(new LayoutParams(itemWidth, itemWidth));
		setOnClickListener(new ItemImageViewOnClickListener());
	}
	
	public int getState() {
		return state;
	}
	
	public int getRow() {
		return row;
	}

	public int getPos() {
		return pos;
	}
	
	public void setItemImageBroken() {
		setImageDrawable(drItemBroken);
		game.removeItem(row, pos);
		state = 0;
		requestLayout();
	}
	
	public void setItemImage() {
		setImageDrawable(drItem);
		state = 1;
		requestLayout();
	}
}
