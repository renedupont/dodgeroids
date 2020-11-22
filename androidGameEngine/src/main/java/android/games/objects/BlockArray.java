package android.games.objects;

public class BlockArray {

	private int amountOfBlockRows;
	private int amountOfBlockSlotsInRow;// length of a row

	private int minLimitOfBlocksOfASide;
	private int maxLimitOfBlocksOfASide;

	private int obstacleSize; // amount of blocks a obstacle consists of (vertically)
	private int newObstacleIndex; // determines when a new obstacle needs to be added to the block array
	private int currentObstacleIndex; // the current index which gets incremented with each new block array

	// until newObstacleIndex is reached

	public int getAmountOfBlockRows() {
		return amountOfBlockRows;
	}

	public void setAmountOfBlockRows(final int amountOfBlockRows) {
		this.amountOfBlockRows = amountOfBlockRows;
	}

	public int getAmountOfBlockSlotsInRow() {
		return amountOfBlockSlotsInRow;
	}

	public void setAmountOfBlockSlotsInRow(final int amountOfBlockSlotsInRow) {
		this.amountOfBlockSlotsInRow = amountOfBlockSlotsInRow;
	}

	public int getNewObstacleIndex() {
		return newObstacleIndex;
	}

	public void setNewObstacleIndex(final int newObstacleIndex) {
		this.newObstacleIndex = newObstacleIndex;
	}

	public int getCurrentObstacleIndex() {
		return currentObstacleIndex;
	}

	public void setCurrentObstacleIndex(final int currentObstacleIndex) {
		this.currentObstacleIndex = currentObstacleIndex;
	}

	public int getMinLimitOfBlocksOfASide() {
		return minLimitOfBlocksOfASide;
	}

	public void setMinLimitOfBlocksOfASide(final int minLimitOfBlocksOfASide) {
		this.minLimitOfBlocksOfASide = minLimitOfBlocksOfASide;
	}

	public int getMaxLimitOfBlocksOfASide() {
		return maxLimitOfBlocksOfASide;
	}

	public void setMaxLimitOfBlocksOfASide(final int maxLimitOfBlocksOfASide) {
		this.maxLimitOfBlocksOfASide = maxLimitOfBlocksOfASide;
	}

	public int getObstacleSize() {
		return obstacleSize;
	}

	public void setObstacleSize(final int obstacleSize) {
		this.obstacleSize = obstacleSize;
	}

}
