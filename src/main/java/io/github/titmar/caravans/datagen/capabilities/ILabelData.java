package io.github.titmar.caravans.datagen.capabilities;

import net.minecraft.util.math.BlockPos;

public interface ILabelData {

	void setMarket(BlockPos marketPos);
	
	BlockPos getMarket();
	
	void setContainer(BlockPos containerPos);
	
	BlockPos getContainer();
	
	
	
}
