package org.michep;

public class ItemsRow {
	private int itemsCount;
	private int extantCount;
	private Item[] items;

	private class Item {
		private int state;

		public Item(int state) {
			this.state = state;
		}

		private int getState() {
			return state;
		}

		private void setState(int state) {
			this.state = state;
		}
	}

	public ItemsRow(int itemsCount) {
		this.itemsCount = itemsCount;
		items = new Item[itemsCount];
		for (int idx = 0; idx < itemsCount; idx++)
			items[idx] = new Item(1);
		updateExtantCount();
	}

	public int getExtantCount() {
		return extantCount;
	}

	public int getItemsCount() {
		return itemsCount;
	}

	private void updateExtantCount() {
		extantCount = 0;
		for (int idx = 0; idx < itemsCount; idx++)
			extantCount += items[idx].getState();
	}

	public void removeItem(int position) {
		items[position].setState(0);
		updateExtantCount();
	}

	public int getItemState(int position) {
		return items[position].getState();
	}
}
