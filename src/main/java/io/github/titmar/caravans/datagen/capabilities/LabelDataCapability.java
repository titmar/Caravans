package io.github.titmar.caravans.datagen.capabilities;

import net.minecraft.util.math.BlockPos;

public class LabelDataCapability implements ILabelData {

	private BlockPos market;
	private BlockPos container;

	@Override
	public void setMarket(BlockPos marketPos) {
		this.market = marketPos;
	}

	@Override
	public BlockPos getMarket() {
		return this.market;
	}

	@Override
	public void setContainer(BlockPos containerPos) {
		this.container = containerPos;
	}

	@Override
	public BlockPos getContainer() {
		return this.container;
	}

}
