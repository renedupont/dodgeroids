package de.games.engine.objects;

public class BlockRow {

    private enum Direction {
        DOWN,
        STAYED_SAME,
        UP
    }

    /*
     * class elements
     */
    private Block[] blocks;

    private BlockArray parent;
    private BlockRow previous;

    //	private int amountOfFilledBlockSlotsInRow; // amount of actual blocks in the row; without the
    // obstacles
    private int amountOfBottomBlocks; // the amount of blocks that are on the bottom, from here
    // the amount the ceiling will have can be calculated since
    // the free space always needs to stay the same
    private int amountOfTopBlocks;
    private boolean hasObstacle;

    private Direction
            direction; // flag which informs if row free space went up, stayed the same or went down
    // one in relation to the previous one

    //	private boolean wentUp;

    /*
     * ctors
     */
    public BlockRow(
            final BlockArray parent,
            final BlockRow previous,
            //			final int amountOfFilledBlockSlotsInRow,
            final int amountOfBottomBlocks,
            final int amountOfTopBlocks,
            final boolean hasObstacle) {
        super();
        this.parent = parent;
        this.previous = previous;
        //		this.amountOfFilledBlockSlotsInRow = amountOfFilledBlockSlotsInRow;
        this.amountOfBottomBlocks = amountOfBottomBlocks;
        this.amountOfTopBlocks = amountOfTopBlocks;
        this.hasObstacle = hasObstacle;

        this.blocks = calculateNewBlockRow();
    }

    public Block[] calculateNewBlockRow() {
        Block[] newBlockRow = new Block[parent.getAmountOfBlockSlotsInRow()];
        if (previous != null) {
            // determine the direction with respect to the previous block row
            switch (previous.getDirection()) {
                case UP:
                    if (previous.getAmountOfTopBlocks()
                            == parent.getMinLimitOfBlocksOfASide()) { // if previous block row went
                        // up with the free space and
                        // has already reached the min
                        // limit it can has regarding
                        // the top
                        direction =
                                (Math.random() < 0.75f) ? Direction.DOWN : Direction.STAYED_SAME;
                        // TODO hier kann man den shit resetten auf irgendne posi
                    } else {
                        direction =
                                (Math.random() < 0.9f)
                                        ? Direction.UP
                                        : ((Math.random() < 0.5f)
                                                ? Direction.STAYED_SAME
                                                : Direction.DOWN);
                    }
                    break;
                case DOWN:
                    if (previous.getAmountOfTopBlocks() == parent.getMaxLimitOfBlocksOfASide()) {
                        direction = (Math.random() < 0.75f) ? Direction.UP : Direction.STAYED_SAME;
                    } else {
                        direction =
                                (Math.random() < 0.9f)
                                        ? Direction.DOWN
                                        : ((Math.random() < 0.5f)
                                                ? Direction.STAYED_SAME
                                                : Direction.UP);
                    }
                    break;
                case STAYED_SAME:
                    if (previous.getAmountOfTopBlocks() == parent.getMinLimitOfBlocksOfASide()) {
                        direction =
                                (Math.random() < 0.75f) ? Direction.DOWN : Direction.STAYED_SAME;
                    } else if (previous.getAmountOfTopBlocks()
                            == parent.getMaxLimitOfBlocksOfASide()) {
                        direction = (Math.random() < 0.75f) ? Direction.UP : Direction.STAYED_SAME;
                    } else {
                        direction = (Math.random() < 0.5f) ? Direction.UP : Direction.DOWN;
                    }
                    break;
            }
            // set block values with respect to the just determined direction
            switch (direction) {
                case UP:
                    amountOfBottomBlocks = previous.getAmountOfBottomBlocks() + 1;
                    amountOfTopBlocks = previous.getAmountOfTopBlocks() - 1;
                    break;
                case DOWN:
                    amountOfBottomBlocks = previous.getAmountOfBottomBlocks() - 1;
                    amountOfTopBlocks = previous.getAmountOfTopBlocks() + 1;
                    break;
                case STAYED_SAME:
                    amountOfBottomBlocks = previous.getAmountOfBottomBlocks();
                    amountOfTopBlocks = previous.getAmountOfTopBlocks();
                    break;
            }
            // set blocks
            for (int i = 0; i < blocks.length; i++) {}

        } else {

        }

        return newBlockRow;
    }

    /*
     * getter & setter
     */
    public Block[] getBlocks() {
        return blocks;
    }

    public void setBlocks(final Block[] blocks) {
        this.blocks = blocks;
    }

    public BlockArray getParent() {
        return parent;
    }

    public void setParent(final BlockArray parent) {
        this.parent = parent;
    }

    public BlockRow getPrevious() {
        return previous;
    }

    public void setPrevious(final BlockRow previous) {
        this.previous = previous;
    }

    public int getAmountOfBottomBlocks() {
        return amountOfBottomBlocks;
    }

    public void setAmountOfBottomBlocks(final int amountOfBottomBlocks) {
        this.amountOfBottomBlocks = amountOfBottomBlocks;
    }

    public int getAmountOfTopBlocks() {
        return amountOfTopBlocks;
    }

    public void setAmountOfTopBlocks(final int amountOfTopBlocks) {
        this.amountOfTopBlocks = amountOfTopBlocks;
    }

    public boolean isHasObstacle() {
        return hasObstacle;
    }

    public void setHasObstacle(final boolean hasObstacle) {
        this.hasObstacle = hasObstacle;
    }

    //	public int getAmountOfFilledBlockSlotsInRow() {
    //		return amountOfFilledBlockSlotsInRow;
    //	}
    //
    //	public void setAmountOfFilledBlockSlotsInRow(
    //			final int amountOfFilledBlockSlotsInRow) {
    //		this.amountOfFilledBlockSlotsInRow = amountOfFilledBlockSlotsInRow;
    //	}

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(final Direction direction) {
        this.direction = direction;
    }
}
